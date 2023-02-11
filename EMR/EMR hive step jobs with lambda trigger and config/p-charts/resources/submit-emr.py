import os, json,boto3,math
from datetime import datetime

CONFIG_ITEM_KEY='resources/cfg.json'
today = datetime.now()
NA='NOT_AVAILABLE'
data_bucket=NA
code_bucket=NA
es_host=NA
es_replicas=0
es_charts_alias=NA
es_bhav_copy_alias=NA
number_of_nodes=2
holdings_details_file_name=''
env='dev'

def lambda_handler(event, context):
    global today
    global data_bucket
    global code_bucket
    global es_host
    global es_replicas
    global es_charts_alias
    global es_bhav_copy_alias
    global number_of_nodes
    global holdings_details_file_name

    initializeConfigValues()
    holdings_details_file = holdings_details_file_name
    file = event['Records'][0]['s3']['object']['key']
    file_name = data_bucket+file
    file_size=event['Records'][0]['s3']['object']['size']
    #file_size is in bytes
    file_size_in_gbs=findSizeInGBs(file_size)
    number_of_nodes=4*file_size_in_gbs
    if number_of_nodes > 30:
        number_of_nodes=30
    es_index='portfolio-charts-'+today.strftime("%m-%d-%Y-%H-%M-%S")
    print("S3 file name : %s, Es Host: %s, Es index : %s , Es Replicas",file_name,es_host, es_replicas, es_index)

    connection = boto3.client('emr', region_name='ap-south-1')

    cluster_id = connection.run_job_flow(
    Name='portfolio-charts-'+os.getenv("ENV"),
    ReleaseLabel='emr-5.29.0',
    LogUri='s3n://aws-logs-762730xxxxxx-ap-south-1/elasticmapreduce/',
    Instances={
        'InstanceGroups': [
            {
                'Name': "Master nodes",
                'Market': 'ON_DEMAND',
                'InstanceRole': 'MASTER',
                'InstanceType': 'm5.xlarge',
                'InstanceCount': 1,
                "EbsConfiguration": {
                    'EbsOptimized': True,
                    "EbsBlockDeviceConfigs": [
                    {
                      "VolumeSpecification": {
                        "Iops": 2000,
                        "VolumeType": "io1",
                        "SizeInGB": 200
                      },
                      "VolumesPerInstance": 1
                    }
                  ]
                }
            },
            {
                'Name': "Slave nodes",
                'Market': 'ON_DEMAND',
                'InstanceRole': 'CORE',
                'InstanceType': 'm5.xlarge',
                'InstanceCount': int(number_of_nodes),
                'EbsConfiguration': {
                    'EbsOptimized': True,
                    "EbsBlockDeviceConfigs": [
                    {
                      "VolumeSpecification": {
                        "Iops": 6000,
                        "VolumeType": "io1",
                        "SizeInGB": 1000
                      },
                      "VolumesPerInstance": 1
                    }
                  ]
                }
            }
        ],
        'KeepJobFlowAliveWhenNoSteps': False,
        'TerminationProtected': True,
        'Ec2SubnetId': 'subnet-082b26ddxxxxx53b',
        'Ec2KeyName':'loadtest'
    },
    Steps=[
        {
            'Name': 'hive-scrips.sh',
            'ActionOnFailure': 'TERMINATE_CLUSTER',
            'HadoopJarStep': 
                {   
                    'Jar': 's3://ap-south-1.elasticmapreduce/libs/script-runner/script-runner.jar',
                    'Args': [ "s3://holdings-poc/resources/hive-scripts.sh", file_name, code_bucket, holdings_details_file ] 
                } 
        },
        {
            'Name': 'create-index.sh',
            'ActionOnFailure': 'TERMINATE_CLUSTER',
            'HadoopJarStep': 
                {   
                    'Jar': 's3://ap-south-1.elasticmapreduce/libs/script-runner/script-runner.jar',
                    'Args': [ "s3://holdings-poc/resources/create-index.sh", es_host, es_replicas, es_index ] 
                } 
        },
         {
            'Name': 'holdings.hql',
            'ActionOnFailure': 'TERMINATE_CLUSTER',
            'HadoopJarStep': 
                {   
                    'Jar': 'command-runner.jar',
                    'Args': [ "hive","-f","s3://holdings-poc/resources/holdings.hql","-d","es_bhav_copy_alias="+es_bhav_copy_alias,"-d","es_host="+es_host,"-d","es_index="+es_index] 
                } 
        },
        {
            'Name': 'delete-index-change-alias.sh',
            'ActionOnFailure': 'TERMINATE_CLUSTER',
            'HadoopJarStep': 
                {   
                    'Jar': 's3://ap-south-1.elasticmapreduce/libs/script-runner/script-runner.jar',
                    'Args': [ "s3://holdings-poc/resources/delete-index-change-alias.sh", es_host, es_charts_alias, es_index, es_replicas ] 
                } 
        }
    ], 
    BootstrapActions= [
        {
            "Name": "upload-hive-jar",
            "ScriptBootstrapAction": {
                "Path": "s3://holdings-poc/resources/bootstrap.sh"
            }
        }
    ],
    VisibleToAllUsers=True,
    JobFlowRole='EMR_EC2_DefaultRole',
    Applications=[{'Name':'Hadoop'},{'Name':'Hive'}],
    Configurations=[
      {
        "Classification": "hive-site",
        "Properties": {
          "hive.server2.tez.sessions.per.default.queue": "5",
          "hive.exec.parallel":"true",
          "hive.exec.dynamic.partition":"true",
          "hive.exec.dynamic.partition.mode":"nonstrict",
          "hive.exec.max.dynamic.partitions.pernode":"20000",
          "hive.exec.max.dynamic.partitions":"500000",
          "hive.auto.convert.join.noconditionaltask" : "true",
          "hive.auto.convert.join.noconditionaltask.size": "209715200",
          "hive.optimize.bucketmapjoin":"true",
          "mapred.reduce.tasks":"50",
          "hive.tez.auto.reducer.parallelism":"rue"
        }
      }
    ],
    ServiceRole='EMR_DefaultRole',
    Tags=[
        {
            "Key":"name",
            "Value":"prortfolio-charts"
        }
    ])

    return {
        'statusCode': 200,
        'body': file_name
    }

def initializeConfigValues():

    global today
    global data_bucket
    global code_bucket
    global es_host
    global es_replicas
    global es_charts_alias
    global es_bhav_copy_alias
    global number_of_nodes
    global holdings_details_file_name

    S3_CONFIG_BUCKET_NAME=os.getenv('S3_CONFIG_BUCKET_NAME')
    env=os.getenv('ENV')

    s3 = boto3.resource('s3')
    obj = s3.Object(S3_CONFIG_BUCKET_NAME, CONFIG_ITEM_KEY)
    CONFIG_FILE = obj.get()['Body'].read()
    #print(CONFIG_FILE)
    CONFIG_JSON = json.loads(CONFIG_FILE)

    data_bucket = CONFIG_JSON[env]['data_bucket']
    code_bucket = CONFIG_JSON[env]['code_bucket']

    es_host = CONFIG_JSON[env]['es_host']
    es_replicas = CONFIG_JSON[env]['es_replicas']
    es_charts_alias = CONFIG_JSON[env]['es_charts_alias']
    es_bhav_copy_alias=CONFIG_JSON[env]['es_bhav_copy_alias']
    holdings_details_file_name=CONFIG_JSON[env]['holdings_details_file']

    print("data_bucket :",data_bucket)
    print("code_bucket:",code_bucket)
    print("today :",today)
    print("es_host :",es_host)
    print("es_replicas :",es_replicas)
    print("es_charts_alias :",es_charts_alias)
    print("es_bhav_copy_alias :",es_bhav_copy_alias)

#end of configuration values initialisation

def findSizeInGBs(size_in_bytes):
    bytes_in_1_GB=1024*1024*1024
    size_in_gb=int(size_in_bytes/bytes_in_1_GB)+1
    return size_in_gb




   
