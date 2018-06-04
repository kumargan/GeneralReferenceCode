var exec = require('child_process').exec;
var streamName = 'stream-name';
var profile = 'default';

var defaultShardId = process.env.defaultShardId;
if(!defaultShardId)
	defaultShardId='00';

var cmd = 'aws kinesis get-shard-iterator --shard-id shardId-0000000000'+defaultShardId+' --shard-iterator-type TRIM_HORIZON --region us-east-1  --stream-name '+streamName+' --profile='+profile+' --region="us-east-1"';
var cmd2 = 'aws kinesis get-records --region us-east-1 --shard-iterator ';
var addParams = ' --profile='+profile+' --region="us-east-1"';

function read(){
	exec(cmd, function(error, stdout, stderr) {
	console.log(stdout);
		var nextCusrsor = JSON.parse(stdout).ShardIterator;
		fun1(nextCusrsor);
	});
}

function fun1(cursor){
exec(cmd2+cursor+addParams, function(error, stdout, stderr) {
	console.log("empty shard");
	console.log(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::"+stdout);
	if(JSON.parse(stdout).Records.length>0){
		var recs = JSON.parse(stdout).Records;
		for(i = 0; i < recs.length; ++i) {
			payload = new Buffer(recs[i].Data, 'base64').toString('ascii');
			console.log('\nDecoded payload: ' + payload);
		}
		console.log('::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::');
	}
		var nextCusrsor = JSON.parse(stdout).NextShardIterator;
		fun1(nextCusrsor);
	});
}

read();