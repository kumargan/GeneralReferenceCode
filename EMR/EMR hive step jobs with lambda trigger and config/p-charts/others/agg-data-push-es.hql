DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS AS SELECT ucc,'agg_isin' isin,bdate,sum(price) price from ISIN_USER group by ucc,bdate;
select count(*),"USERS" from USERS;

DROP TABLE IF EXISTS ALL_DETAILS;
CREATE TABLE ALL_DETAILS AS select * from ISIN_USER UNION select * from USERS;
select count(*),"ALL_DETAILS" from ALL_DETAILS;


DROP TABLE IF EXISTS portfoliocharts;
CREATE EXTERNAL TABLE portfoliocharts(ucc STRING,isin STRING,bdate timestamp,value FLOAT) STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler' TBLPROPERTIES('es.resource'='${es_index}','es.nodes'='${es_host}');
INSERT OVERWRITE TABLE portfoliocharts SELECT * FROM ALL_DETAILS;