DROP TABLE IF EXISTS bhav_copy_external;
CREATE EXTERNAL TABLE IF NOT EXISTS bhav_copy_external (isin STRING,exchdt STRING,c DOUBLE,exch STRING) STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
TBLPROPERTIES('es.resource' = '${es_bhav_copy_alias}','es.nodes'='${es_host}','es.mapping.names' = 'c:Close,exch:exchange,exchdt:Date','es.mapping.date.rich'='false');

DROP TABLE IF EXISTS bhav_copy_temp_1;
CREATE TABLE bhav_copy_temp_1 as select isin,c,exch,exchdt from bhav_copy_external where isin IS NOT null;
select count(*),"bhav_copy_temp_1",current_timestamp from bhav_copy_temp_1;

DROP TABLE IF EXISTS bhav_copy_temp_2;
CREATE TABLE bhav_copy_temp_2 as select isin,c,exch,from_unixtime(unix_timestamp(exchdt,'dd-MM-yyyy HH:mm')) exchdt,row_number() over (partition by isin,exchdt order by exch) as rownum from bhav_copy_temp_1 ;
select count(*),"bhav_copy_temp_2",current_timestamp from bhav_copy_temp_2;
select * from bhav_copy_temp_2 limit 10;

DROP TABLE IF EXISTS bhav_copy;
CREATE TABLE bhav_copy as select * from bhav_copy_temp_2 where rownum = 1;

select count(*),"bhav_copy",current_timestamp from bhav_copy;

--DROP FUNCTION GetMinIndex;
--CREATE FUNCTION GetMinIndex AS 'com.paytmmoney.hive.udf.GetMinIndex';

--Read holdings data from S3 for manual testing
--DROP TABLE IF EXISTS holdings_ext;
--CREATE EXTERNAL TABLE IF NOT EXISTS holdings_ext (Asset_Class STRING,As_On_Date STRING,UCC STRING,Purchase_Date STRING,ISIN STRING,Scrip_Name STRING,Quantity INT,Purchase_Price FLOAT,Purchase_Value FLOAT,Market_Price FLOAT,Market_Value FLOAT,Unrealized_Gain_Loss_Amount FLOAT) 
--ROW FORMAT DELIMITED FIELDS TERMINATED BY '|' LOCATION 's3n://holdings-poc/holdings' TBLPROPERTIES ("skip.header.line.count"="1");

--Read holdings data from HDFS for step JOBS
DROP TABLE IF EXISTS holdings_ext;
CREATE EXTERNAL TABLE IF NOT EXISTS holdings_ext (Asset_Class STRING,As_On_Date STRING,UCC STRING,Purchase_Date STRING,ISIN STRING,Scrip_Name STRING,Quantity INT,Purchase_Price FLOAT,Purchase_Value FLOAT,Market_Price FLOAT,Market_Value FLOAT,Unrealized_Gain_Loss_Amount FLOAT) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '|' LOCATION '/holdings/' TBLPROPERTIES ("skip.header.line.count"="1");


DROP TABLE IF EXISTS holdings;
CREATE TABLE IF NOT EXISTS holdings as select isin,ucc,from_unixtime(unix_timestamp(Purchase_Date,'dd/MM/yyyy')) sauda,quantity qty from holdings_ext;
select count(*),"holdings",current_timestamp from holdings;
select * from holdings limit 10;

DROP TABLE IF EXISTS ISIN_USER;
CREATE TABLE ISIN_USER AS SELECT h.ucc ucc,h.isin isin ,b.exchdt bdate,SUM(qty)*COLLECT_LIST(c)[0] price FROM holdings h JOIN bhav_copy b ON (h.isin = b.isin AND h.sauda <= b.exchdt) GROUP BY h.ucc,h.isin,b.exchdt;
select count(*),"ISIN_USER", current_timestamp from ISIN_USER;

DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS AS SELECT ucc,'agg_isin' isin,bdate,sum(price) price from ISIN_USER group by ucc,bdate;
select count(*),"USERS", current_timestamp from USERS;

--DROP TABLE IF EXISTS ALL_DETAILS;
--CREATE TABLE ALL_DETAILS AS select * from ISIN_USER UNION select * from USERS;
--select count(*),"ALL_DETAILS", current_timestamp from ALL_DETAILS;

--DROP TABLE IF EXISTS ALL_DETAILS;
--CREATE TABLE ALL_DETAILS (ucc STRING,isin STRING,bdate DATE,price FLOAT);
--INSERT INTO ALL_DETAILS SELECT * FROM ISIN_USER;
--INSERT INTO ALL_DETAILS SELECT * FROM USERS;
--select count(*),"ALL_DETAILS", current_timestamp from ALL_DETAILS;

DROP TABLE IF EXISTS portfoliocharts;
CREATE EXTERNAL TABLE portfoliocharts(ucc STRING,isin STRING,bdate timestamp,value FLOAT) STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler' TBLPROPERTIES('es.resource'='${es_index}','es.nodes'='${es_host}','es.mapping.routing'='ucc');
INSERT INTO TABLE portfoliocharts SELECT * FROM ISIN_USER;
INSERT INTO TABLE portfoliocharts SELECT * FROM USERS;
select "portfolio charts done ",current_timestamp;