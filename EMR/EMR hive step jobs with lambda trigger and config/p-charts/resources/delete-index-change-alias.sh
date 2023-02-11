#!/bin/bash

host=$1
alias=$2
new_index=$3
replica=$4

#Get index from alias
old_index=`curl -XGET $host"_cat/aliases/"$alias"?format=JSON" | jq -r  '.[0].index'`
echo "old index "$old_index
echo "............................."

echo "update setting for new index "$new_index" replica "$replica
echo "............................."

curl -XPUT $host$new_index"/_settings" -H 'Content-Type: application/json' -d'
{
	"index":{
		"number_of_replicas":'$replica',
		"refresh_interval":"1s"
	}
}'
echo "............................."
## if old alias exists
	if [ -z "$old_index" ] & [ "$old_index" = "null" ]
	then
	  #create new alias
	  echo "............................."
	  echo "creating new alias "$new_index
	  echo "............................."
	  curl -XPOST $host"_aliases" -H 'Content-Type: application/json' -d'
		{
		  "actions": [
		    {
		      "add": {
		        "index": "'$new_index'",
		        "alias": "'$alias'"
		      }
		    }
		  ]
		}'
	else
	  #Change alias to new index
	  echo "............................."
	  echo "updating alias old index"$old_index" new index "$new_index
	  echo "............................."
	  curl -XPOST $host"_aliases" -H 'Content-Type: application/json' -d'
		{
		  "actions": [
		    {
		      "remove": {
		        "index": "'$old_index'",
		        "alias": "'$alias'"
		      }
		    },
		    {
		      "add": {
		        "index": "'$new_index'",
		        "alias": "'$alias'"
		      }
		    }
		  ]
		}'
	
		#Delete old index
		echo "deleting old index "$old_index
		echo "............................."
	  	curl -XDELETE $host$old_index
	fi




