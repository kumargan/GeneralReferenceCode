// dependencies
//var async = require('async');
var AWS = require('aws-sdk');
var util = require('util');
// Create the DynamoDB service object
var ddb = new AWS.DynamoDB.DocumentClient();

// get reference to S3 client 
var s3 = new AWS.S3();
 //read file from S3( File name is received in event obj ). Calculate max, min, sum and send the same in dynamo db table
 // lambda1 ---- test1-lambda
exports.handler = function(event, context, callback) {
    // Read options from the event.
    //console.log("Reading options from event:\n", util.inspect(event, {depth: 5}));
    var srcBucket = event.Records[0].s3.bucket.name;
    // Object key may have spaces or unicode non-ASCII characters.
    var srcKey    = event.Records[0].s3.object.key;
    console.log("srcBucket "+srcBucket+" srcKey "+srcKey);

    // Download the file from S3
   
          var file =   s3.getObject({
                    Bucket: srcBucket,
                    Key: srcKey
                },function(err, data) {
                    if (err) console.log(err, err.stack); // an error occurred
                    else {
                        //console.log("file contents "+data.Body.toString());           // successful response
                        
                        var contents = data.Body.toString().split('\n');
                        
                        var number = parseInt(contents[0]);
                        var max=number, min=number, sum=number;
                        for(var i=0;i < contents.length-1 ;i++){
                            number = parseInt(contents[i]);
                            sum=sum + number;
                            
                            if(max<number)
                                max= number;
                                
                            if(number < min )
                                min = number;
                        }
                        
                        console.log("max "+max+ " min "+ min +" sum "+sum);  
                        
                        var objectItem = {
                                "file_name" : srcKey,
                                "sum" : sum,
                                'max': max,
                                'min': min
                            };
                            
                        console.log("object item "+JSON.stringify(objectItem));    
                        
                        var params = {
                                TableName: 'lambda_create_dynamo_entryGanesh'
                            };
                        params.Item = objectItem;    
                            
                        ddb.put(params, function(err, data) {
                          if (err) {
                            console.log("Error", err);
                          } else {
                            console.log("Success", data);
                          }
                        });
                        
                    }
                    
                });
                
                
                
############## test event :

{
  "Records": [
    {
      "eventVersion": "2.0",
      "eventSource": "aws:s3",
      "awsRegion": "us-west-2",
      "eventTime": "1970-01-01T00:00:00.000Z",
      "eventName": "ObjectCreated:Put",
      "userIdentity": {
        "principalId": "AIDAJDPLRKLG7UEXAMPLE"
      },
      "requestParameters": {
        "sourceIPAddress": "127.0.0.1"
      },
      "responseElements": {
        "x-amz-request-id": "C3D13FE58DE4C810",
        "x-amz-id-2": "FMyUVURIY8/IgAtTv8xRjskZQpcIZ9KG4V5Wp6S7S/JRWeUWerMUE5JgHvANOjpD"
      },
      "s3": {
        "s3SchemaVersion": "1.0",
        "configurationId": "testConfigRule",
        "bucket": {
          "name": "dynamo-s3-bucket-ganesh",
          "ownerIdentity": {
            "principalId": "A3NL1KOZZKExample"
          },
          "arn": "arn:aws:s3:::dynamo-s3-bucket-ganesh"
        },
        "object": {
          "key": "1.txt",
          "size": 1024,
          "eTag": "d41d8cd98f00b204e9800998ecf8427e",
          "versionId": "096fKKXTRTtl3on89fVO.nfljtsv6qko"
        }
      }
    }
  ]
}
               
};