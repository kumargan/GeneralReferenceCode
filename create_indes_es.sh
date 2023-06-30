#!bin bash

host=$1
if [ -z "$host" ]
then
	echo "no host passed, trying with default host 127.0.0.1:9200"
    host='127.0.0.1:9200'
fi

echo -e "************************************************************************************\n\n\n"

## Create mapping for index_name index
echo "creating main index index_name ...."
sh scripts/index_name.sh $host

##Create aliases for the index_name index
echo "\n creating aliases for index_name ...."
curl -XPOST $host'/_aliases' -H 'Content-Type: application/json' -d '{
    "actions" : [
        {"add" : { "index" : "index_name", "alias" : "index_name_alias" } }
    ]
}'

echo -e "************************************************************************************\n\n\n"
