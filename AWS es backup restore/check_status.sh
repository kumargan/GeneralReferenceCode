#!/bin/bash

while true
do
  status=`curl -XGET 'https://search--tvzve6vt3m4u3e7flmid5id7ui.us-east-1.es.amazonaws.com/_snapshot/es-index-backups/_all?pretty' | jq '.snapshots[0].state'`
  echo "status is $status"
  if [ "$status" == "\"IN_PROGRESS\"" ]; then
  	echo "not completed "
  	sleep 60 &
  	wait
  else
  	echo "***************************** Completed ********************" 
  	break;
  fi
done