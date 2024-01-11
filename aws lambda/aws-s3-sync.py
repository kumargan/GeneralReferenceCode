
import boto3
import botocore
import json
import os
import logging
logger = logging.getLogger()
logger.setLevel(logging.INFO)

s3 = boto3.resource('s3')

def lambda_handler(event, context):
    logger.info("New files uploaded to the source bucket.")
    print(event)
        
    key = event['Records'][0]['s3']['object']['key']
    eventName = event['Records'][0]['eventName']
        
    source_bucket = event['Records'][0]['s3']['bucket']['name']
    destination_bucket = os.environ['destination_bucket']
    
    print("destination_bucket.  "+destination_bucket)
    print("Bucket: " + source_bucket + " Key :" + key)
    
    source = {'Bucket': source_bucket, 'Key': key}
        
        
    try:
        
        
        if (eventName=="ObjectRemoved:Delete"):
            response = s3.Object(destination_bucket, "test_sync/"+key).delete()
            logger.info("File deleted from destination bucket successfully!")
        else:
            response = s3.meta.client.copy(source, destination_bucket, "test_sync/"+key)
            logger.info("File copied to the destination bucket successfully!")
        
    except botocore.exceptions.ClientError as error:
        logger.error("There was an error copying the file to the destination bucket")
        print('Error Message: {}'.format(error))
        
    except botocore.exceptions.ParamValidationError as error:
        logger.error("Missing required parameters while calling the API.")
        print('Error Message: {}'.format(error))