
#### host=http://es-apps.dev.equity:80/

host=$1
replica=$2
index=$3


curl -XPUT $host$index -H 'Content-Type: application/json' -d'
{
  "settings": {
    "number_of_shards": 6,
    "number_of_replicas": 0,
    "refresh_interval":-1
  },
  "mappings": {
    "properties": {
      "isin": {
        "type": "keyword"
      },
      "bdate": {
        "type": "date"
      },
      "ucc": {
        "type": "keyword"
      },
      "value": {
        "type": "float"
      }
    }
  }
}'