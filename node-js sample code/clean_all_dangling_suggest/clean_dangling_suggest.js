var elasticsearch = require('elasticsearch');
var ElasticsearchScrollStream = require('elasticsearch-scroll-stream');
var sleep = require('thread-sleep');
var request = require('request');
var endpoint = 'http://localhost:9201'
var client = new elasticsearch.Client({
  host: endpoint
});

var stopCounterIndex = 1000000;
var counter = 0;
var delete_lot_size = 10;
var ids = [];
var deleteids = [];
var index_name_sugg = 'b2b_suggestions_alias';
var index_name_search = 'b2b_products_alias';
var scan_complete=false;
var deleted_dangling_docs=0;

//delete the dangling suggest docs 
var deleteFlag = false;

 
var es_stream = new ElasticsearchScrollStream(client, {
  index: index_name_sugg,
  type: '_doc',
  scroll: '10s',
  size: '20',
  _source:['uuid'],
  body: {
     query: {
         "match_all": {}
     }
  }
}, []); // optional_fields parameter: allowed values are those supported by elasticsearch
 
es_stream.on('data', function(data) {
  var current_doc = JSON.parse(data.toString());
  //console.log('reading data from stream ',current_doc);
  ids.push(current_doc.uuid);
  //run strean every few milliseconds
  //sleep(1000);

  if (counter == stopCounterIndex) {
    es_stream.close();
  }
  counter++;
});

es_stream.on('end', function() {
  checkIfDangling(ids)
  console.log("total documets scanned",counter);
  scan_complete=true;
});
 
es_stream.on('error', function(err) {
  console.log(err);
});

// following code runs every second and submits no of requests = delete_lot_size for checking if it is dandling.
setInterval(checkIds,1000);

function checkIds(){
	if(ids.length==delete_lot_size){
  	checkIfDangling(ids.splice(0,delete_lot_size));
  }
}



var fn = function isIdDangling(v){ // sample async action
    return new Promise((resolve,reject)=>{
    		var post_data = {
    							"_source":false,
  								"query":{
        						"match":{"suggest_ids":v}
              					}
							}

				request.post({
				  url:  endpoint+'/'+index_name_search+'/_doc/_search',
				  json:    post_data
				}, function(error, response, body){
					//console.log('********************************',error,body);
				  	resolve({"id":v,"count":body.hits.total});
				});
				//console.log('made query ',post_data);
    });
	};



function checkIfDangling(testIds){
	//console.log('testing for dangling suggest ',testIds);
	
	// map over forEach since it returns
	var actions = testIds.map(fn); // run the function over all items
	Promise.all(actions).then((results)=>{
		//console.log('results ',results.length);
		for(i=0;i<results.length;i++){
			var result = results[i];
			//console.log('result',JSON.stringify(result));
			if(result.count==0)
				deleteids.push(result.id);
		}
	}); 
	
}


// following code deletes the ids which are dangling from the array deleteids
setInterval(deleteIdsByQuery,1000);

function deleteIdsByQuery(){

	if(deleteids.length==0 && scan_complete){
		if(deleteFlag)
			console.log("total deleted dangling suggest docs ",deleted_dangling_docs);
		else
			console.log("total dangling suggest docs ",deleted_dangling_docs);
		process.exit();
	}

	var deleteids_part = deleteids.splice(0,100);
	if(deleteids_part.length > 0){
		console.log(" inside deleteIdsByQuery "+deleteids_part);
		deleted_dangling_docs+=deleteids_part.length;
			if(deleteFlag){
				var post_data = {
			  						"query":{
			    					"terms":{"_id":deleteids_part}
			  						}
								}

				request.post({
				  url:  endpoint+'/'+index_name_sugg+'/_doc/_delete_by_query',
				  json:    post_data
				}, function(error, response, body){
				  console.log(body);
				});
			}
		}
	}

