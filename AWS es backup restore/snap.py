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
            host='search-customer-insights-tvzve6vt3m4u3e7flmid5id7ui.us-east-1.es.amazonaws.com',
            aws_access_key_id='xxxxxxxxxx',
            aws_secret_access_key='xxxxxxxxxxxx',is_secure=False)

    print 'Registering Snapshot Repository'
    resp = client.make_request(method='PUT',
            path='/_snapshot/es-index-backups',
            data='{"type": "s3","settings": { "bucket": "es-snap-shot-delete-by-2018-02-09","region": "us-east-1","role_arn": "arn:aws:iam::650xxxxxxxx:role/es-dump-keep-for-future-use"}}')
    body = resp.read()
    print body