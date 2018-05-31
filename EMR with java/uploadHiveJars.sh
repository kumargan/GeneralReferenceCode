# copying elasticsearch indexer plugin jar
sudo aws s3 cp s3://bucket/config/jars/elasticsearch-hadoop-5.5.2.jar /usr/lib/hive-hcatalog/share/hcatalog/elasticsearch-hadoop-5.5.2.jar

# copying brickhouse jar
sudo aws s3 cp s3://bucket/config/jars/brickhouse-0.7.1-SNAPSHOT.jar /usr/lib/hive-hcatalog/share/hcatalog/brickhouse-0.7.1-SNAPSHOT.jar


# copying idlp segment hive udf jars
sudo aws s3 cp s3://bucket/config/jars/hiveCustomUDF-1.0.jar /usr/lib/hive-hcatalog/share/hcatalog/hiveCustomUDF-1.0.jar




