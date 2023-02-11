
--set hive.auto.convert.join.noconditionaltask = true;
--set hive.auto.convert.join.noconditionaltask.size = 209715200;
--set hive.optimize.bucketmapjoin=true;
--set mapred.reduce.tasks = 30;
--set hive.tez.auto.reducer.parallelism=true;



--------------------- pull holdings data -------------------

DROP TABLE IF EXISTS holdings_ext;
CREATE EXTERNAL TABLE IF NOT EXISTS holdings_ext (Asset_Class STRING,As_On_Date STRING,UCC STRING,Purchase_Date STRING,ISIN STRING,Scrip_Name STRING,Quantity INT,Purchase_Price FLOAT,Purchase_Value FLOAT,Market_Price FLOAT,Market_Value FLOAT,Unrealized_Gain_Loss_Amount FLOAT) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '|' LOCATION '/holdings/' TBLPROPERTIES ("skip.header.line.count"="1");


DROP TABLE IF EXISTS holdings_temp;
CREATE TABLE IF NOT EXISTS holdings_temp as select ucc,from_unixtime(unix_timestamp(Purchase_Date,'dd/MM/yyyy')) sauda,quantity qty,isin from holdings_ext;
select count(*),"holdings" from holdings_temp;


DROP TABLE IF EXISTS holdings;
CREATE TABLE IF NOT EXISTS holdings ( ucc STRING,sauda date,qty int,isin string) CLUSTERED BY (isin) SORTED BY (isin) INTO 6000 BUCKETS;
insert into holdings select ucc,sauda,qty,isin from holdings_temp;
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
select count(*),"bhav_copy_temp_3" from bhav_copy_temp_3;


DROP TABLE IF EXISTS bhav_copy;
CREATE TABLE IF NOT EXISTS bhav_copy (c float, exch string, exchdt date,isin string) CLUSTERED BY (isin) SORTED BY (isin) INTO 6000 BUCKETS;
insert into bhav_copy select c,exch,exchdt,isin from bhav_copy_temp_3;
select count(*),"bhav_copy" from bhav_copy;
select * from bhav_copy limit 10;



----------------- create intermediate table for isin_user-------
DROP TABLE IF EXISTS ISIN_USER_temp;
CREATE TABLE ISIN_USER_temp (ucc STRING,isin STRING,bdate TIMESTAMP,c DOUBLE, qty INT);
INSERT OVERWRITE TABLE ISIN_USER_temp SELECT /*+ MAPJOIN(b) */ h.ucc ucc,h.isin isin ,b.exchdt bdate, b.c,h.qty FROM holdings h JOIN bhav_copy b ON (h.isin = b.isin and h.sauda <= b.exchdt);
select count(*),"ISIN_USER_temp" from ISIN_USER_temp;
-------------------------------isin user ---------------------

DROP TABLE IF EXISTS ISIN_USER;
CREATE TABLE ISIN_USER (ucc STRING,isin STRING,bdate TIMESTAMP,c DOUBLE);

INSERT OVERWRITE TABLE ISIN_USER SELECT ucc ,isin , bdate,SUM(qty)*COLLECT_LIST(c)[0] price FROM ISIN_USER_temp GROUP BY h.ucc,h.isin,b.exchdt;
select count(*),"ISIN_USER" from isin_user;

DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS AS SELECT ucc,'agg_isin' isin,bdate,sum(price) price from ISIN_USER group by ucc,bdate;
select count(*),"USERS" from USERS;

DROP TABLE IF EXISTS ALL_DETAILS;
CREATE TABLE ALL_DETAILS AS select * from ISIN_USER UNION select * from USERS;
select count(*),"ALL_DETAILS" from ALL_DETAILS;




