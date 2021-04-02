import json
import os

import hmac
import requests
import hashlib

API_VERSION = 2
API_HOST = 'api.xroom.app'


class PythonAPI:

    def __init__(self, username, secret):
        if username is None or len(username) == 0:
            raise Exception('Username required')
        if secret is None or len(secret) == 0:
            raise Exception('Secret required')

        self.username = username
        self.secret = secret

    def call(self, endpoint, method, payload):
        if endpoint is None or len(endpoint) == 0:
            raise Exception('No endpoint specified')
        if method is None or len(method) == 0:
            raise Exception('No method specified')

        salt = bytes(os.urandom(32).hex(), 'utf-8')

        secret = bytes(self.secret, 'utf-8')

        signature = hmac.new(secret, salt, hashlib.sha256).hexdigest()

        payload_dump = json.dumps({
            'method': method,
            'data': payload
        })

        headers = {
            'Content-Length': str(len(payload_dump)),
            'Content-Type': 'application/json',
            'x-api-version': str(API_VERSION),
            'x-random': salt,
            'x-auth-id': self.username,
            'x-auth-key': str(signature),
        }

        return requests.post(f'https://{API_HOST}/api/{endpoint}', data=payload_dump, headers=headers)
