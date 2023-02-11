df = spark.sql("SELECT isin from distinct_isin") 
df.show();
# Python3 code to iterate over a list 
# Python3 code to iterate over a list 
list1=df.rdd.map(lambda x: x.isin).collect()

print(list1)



list = list1

#list1[:2]

firstLine = 'insert into isin_user SELECT h.ucc ucc,h.isin isin ,b.exchdt bdate,SUM(qty)*COLLECT_LIST(c)[0] price FROM ( select * from holdings where isin = \'$$##$$\' ) h JOIN ( select * from  bhav_copy where isin = \'$$##$$\' ) b ON (h.isin = b.isin AND h.sauda <= b.exchdt) GROUP BY h.ucc,h.isin,b.exchdt'
secondLine = ' UNION ALL SELECT h.ucc ucc,h.isin isin ,b.exchdt bdate,SUM(qty)*COLLECT_LIST(c)[0] price FROM ( select * from holdings where isin = \'$$##$$\' ) h JOIN ( select * from  bhav_copy where isin = \'$$##$$\' ) b ON (h.isin = b.isin AND h.sauda <= b.exchdt) GROUP BY h.ucc,h.isin,b.exchdt'
lastLine = ';'
query='DROP TABLE IF EXISTS ISIN_USER;\nCREATE TABLE isin_user(ucc string,isin string,bdate date,price float);\n'

line='\ninsert into isin_user SELECT h.ucc ucc,h.isin isin ,b.exchdt bdate,SUM(qty)*COLLECT_LIST(c)[0] price FROM ( select * from holdings where isin = \'$$##$$\' ) h JOIN ( select * from  bhav_copy where isin = \'$$##$$\' ) b ON (h.isin = b.isin AND h.sauda <= b.exchdt) GROUP BY h.ucc,h.isin,b.exchdt;';

j = 0;
# Using for loop 
#for i in list: 
#	if j==0:
#		query+=firstLine.replace('$$##$$',i)
#		j=j+1
#	else :
#		query+=secondLine.replace('$$##$$',i)
#		j=j+1

#query += lastLine

for i in list: 
    query+=line.replace('$$##$$',i)

print(query)

dfquery = spark.read.json(sc.parallelize([query]))

dfquery.write.format("text").save("/queries/query.hql")

dfquery.coalesce(1).rdd.saveAsTextFile(s"file://path/to/file")
