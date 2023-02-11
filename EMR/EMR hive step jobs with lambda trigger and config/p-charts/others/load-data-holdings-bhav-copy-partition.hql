set hive.exec.parallel=true
set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;
set hive.exec.max.dynamic.partitions.pernode=20000;
set hive.exec.max.dynamic.partitions=500000;


--------------------- pull holdings data -------------------

DROP TABLE IF EXISTS holdings_ext;
CREATE EXTERNAL TABLE IF NOT EXISTS holdings_ext (Asset_Class STRING,As_On_Date STRING,UCC STRING,Purchase_Date STRING,ISIN STRING,Scrip_Name STRING,Quantity INT,Purchase_Price FLOAT,Purchase_Value FLOAT,Market_Price FLOAT,Market_Value FLOAT,Unrealized_Gain_Loss_Amount FLOAT) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '|' LOCATION '/holdings/' TBLPROPERTIES ("skip.header.line.count"="1");


DROP TABLE IF EXISTS holdings_temp;
CREATE TABLE IF NOT EXISTS holdings_temp as select ucc,from_unixtime(unix_timestamp(Purchase_Date,'dd/MM/yyyy')) sauda,quantity qty,isin from holdings_ext;
select count(*),"holdings" from holdings_temp;

DROP TABLE IF EXISTS holdings;
CREATE TABLE IF NOT EXISTS holdings ( ucc STRING,sauda date,qty int) PARTITIONED by (isin string);
insert into holdings partition (isin) select ucc,sauda,qty,isin from holdings_temp;
select count(*),"holdings" from holdings;
select * from holdings limit 10;

--------------------- pull bhav_copy data -------------------

DROP TABLE IF EXISTS bhav_copy_external;
CREATE EXTERNAL TABLE IF NOT EXISTS bhav_copy_external (isin STRING,exchdt STRING,c DOUBLE,exch STRING) STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
TBLPROPERTIES('es.resource' = 'equity_data_historical_data_alias','es.nodes'='http://es-apps.dev.equity:80/','es.mapping.names' = 'c:Close,exch:exchange,exchdt:Date','es.mapping.date.rich'='false');

DROP TABLE IF EXISTS bhav_copy_temp_1;
CREATE TABLE bhav_copy_temp_1 as select isin,c,exch,exchdt from bhav_copy_external where isin IS NOT null;
select count(*),"bhav_copy_temp_1" from bhav_copy_temp_1;

DROP TABLE IF EXISTS bhav_copy_temp_2;
CREATE TABLE bhav_copy_temp_2 as select isin,c,exch,from_unixtime(unix_timestamp(exchdt,'dd-MM-yyyy HH:mm')) exchdt,row_number() over (partition by isin,exchdt order by exch) as rownum from bhav_copy_temp_1 ;
select count(*),"bhav_copy_temp_2" from bhav_copy_temp_2;
select * from bhav_copy_temp_2 limit 10;

DROP TABLE IF EXISTS bhav_copy_temp_3;
CREATE TABLE bhav_copy_temp_3 as select * from bhav_copy_temp_2 where rownum = 1;
select count(*),"bhav_copy_temp_3" bhav_copy_temp_3;

DROP TABLE IF EXISTS bhav_copy;
CREATE TABLE IF NOT EXISTS bhav_copy (c float, exch string, exchdt date) partitioned by (isin string);
insert into bhav_copy partition (isin) select c,exch,exchdt,isin from bhav_copy_temp_3;
select * from bhav_copy limit 10;


-------------------------------------- dintinct isins in holdings ------------

drop table if exists distinct_isin;
create table distinct_isin as SELECT isin from holdings group by isin;

