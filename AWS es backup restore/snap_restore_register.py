from boto.connection import AWSAuthConnection

class ESConnection(AWSAuthConnection):

    def __init__(self, region, **kwargs):
        super(ESConnection, self).__init__(**kwargs)
        self._set_auth_region_name(region)
        self._set_auth_service_name("es")

    def _required_auth_capability(self):
        return ['hmac-v4']

if __name__ == "__main__":

    client = ESConnection(
            region='us-east-1',
            host='vpc-customer-360-view-x4hbqf64s5jnssmwbnx5qcx3q4.us-east-1.es.amazonaws.com',
            aws_access_key_id='xxxx',
            aws_secret_access_key='xxxxxxxx',is_secure=False)

    print 'Registering Snapshot Repository'
    resp = client.make_request(method='PUT',
            path='/_snapshot/es-index-backups',
            data='{"type": "s3","settings": { "bucket": "es-snap-shot-delete-by-2018-02-09","region": "us-east-1","role_arn": "arn:aws:iam::xxxxxxx:role/es-dump-keep-for-future-use"}}')
    body = resp.read()
    print body