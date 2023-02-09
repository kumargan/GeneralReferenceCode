import boto3
def handler(event,context):
  client = boto3.client('ecs')
  response = client.run_task(
  cluster='web-app-ECS', # name of the cluster
  launchType = 'FARGATE',
  taskDefinition='task-name', # replace with your task definition name and revision
  count = 1,
  platformVersion='LATEST',
  networkConfiguration={
        'awsvpcConfiguration': {
            'subnets': [
                'subnet-id'
            ],
            'assignPublicIp': 'DISABLED'
        }
    })
  return str(response)
