##copy zip file locally and explode
fileName=$1
bucket_name=$2
holdings_file=$3

echo "file name: "$fileName 
echo "bucket_name : "$bucket_name 
echo "holdings_file : "$holdings_file 

sudo aws s3 cp s3://$fileName /tmp/$fileName

echo "current path "`pwd`	
current_path=`pwd`
echo "value in current path "$current_path
cd /tmp
unzip $fileName
## remove this later to read directly from HDFS rather than putting it back into S3
#aws s3 cp $holdings_file s3://$bucket_name/holdings/

hdfs dfs -rm -r -f /holdings
hdfs dfs -mkdir /holdings
hdfs dfs -copyFromLocal /tmp/$holdings_file /holdings/$holdings_file

echo `hdfs dfs -ls /holdings`

echo "switching back path "`pwd`
cd $current_path
echo "switched path "`pwd`

#start/stop hive server for udf's
sudo stop hive-server2
sudo start hive-server2
set hive.cli.print.header=true;

## create index 
#date=`date +%d-%m-%Y`