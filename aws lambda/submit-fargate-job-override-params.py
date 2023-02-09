

import boto3
#refer `https://docs.aws.amazon.com/AmazonECS/latest/APIReference/API_RunTask.html` for more override params
def lambda_handler(event,context):
  client = boto3.client('ecs')
  response = client.run_task(
  cluster='my-cluster', # name of the cluster
  launchType = 'FARGATE',
  taskDefinition='sample-node-js-container:2', # replace with your task definition name and revision
  count = 1,
  platformVersion='LATEST',
  overrides={  
   "containerOverrides":[  
      {  
         "environment":[  
            {  
               "name":"S3_FILE_NAME",
               "value":"file2"
            }
         ],
         "name":"sample-node-js-container" 
      }
   ]
},  #"name":"sample-node-js-container"  is te name of the container created in task
  networkConfiguration={
        'awsvpcConfiguration': {
            'subnets': [
                'subnet-xxxxx'
            ],
            'assignPublicIp': 'ENABLED'
        }
    }) # 'assignPublicIp': 'DISABLED' for private networks
  return str(response)
