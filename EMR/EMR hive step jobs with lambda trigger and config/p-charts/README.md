## portfolio-charts

	Creates portfolio charts data points and saves into elastic search for serving end user. It runs EMR as step job to calculate the values, and then the job itself pushes the calculated data from EMR to elastic search using hive plugin.
	
#  Deployment : 
	1. copy the resources folder in bucket.
	2. deploy resources/submit-emr.py in lambda (lambda should have access to create EMR clusters on demand).
	3. set env variable in lambda as ENV=prod/stg/preprod/dev
	
# PRD : 
	https://wiki.mypaytm.com/display/PM/PRD+-+Portfolio+Charts

# Architecture
	https://wiki.mypaytm.com/display/PM/Portfolio+charts

# Steps
	1. hive-scripts.sh
		1. Copy file from aws s3 to EMR.
		2. Unzip output_details.zip at /tmp.
		3. Copy resources folder in EMR.
		3. Restart hive server.

	2. create-index.sh
		1. Create index with multiple shards in elastic search without any replica.
		
	3. holdings.hql
		1. Pull bhav copy from ES into hive table. ( closing price pulled will be NSE, if NSE price not availabe them BSE)
		2. Load holdings data from output_details.zip (vis HDFS).
		3. Calculate data points using join between the tables.
		4. Insert the data points into elasitc search.

	4. delete-index-change-alias.sh
		1. create replicas for new index
		2. change alias from old index to new one
		3. delete old index.
		
	5. Loading hive functions :

		sudo aws s3 cp s3://etl-artefacts/code/portfolio-charts-hive-udf-0.0.1.jar /usr/lib/hive-hcatalog/share/hcatalog/portfolio-charts-hive-udf-0.0.1.jar
		ADD JAR /usr/lib/hive-hcatalog/share/hcatalog/portfolio-charts-hive-udf-0.0.1.jar;
		CREATE TEMPORARY FUNCTION GetMinIndex AS 'com.company.hive.udf.GetMinIndex';

	6. Sample data 

		create table test(id string,date_ string ,price int) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
		LOAD DATA LOCAL INPATH '/home/hadoop/data.csv' OVERWRITE INTO TABLE test;