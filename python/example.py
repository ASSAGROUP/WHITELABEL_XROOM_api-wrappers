from wrapper import PythonAPI

api = PythonAPI('username', 'secret')

payload = {
    'id': 'your-domain.com/room-name',
}

c = api.call('room', 'init', payload)

answer = {
    'status code': c.status_code,
    'response': c.json()
}

print(answer, flush=True)
