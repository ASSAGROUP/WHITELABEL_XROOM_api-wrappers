from wrapper import PythonAPI

api = PythonAPI('YOUR_USERNAME', 'YOUR_SECRET')

payload = {
        id: 'your-domain.com/hello-world',
        }

c = api.call('room', 'init', payload)

print(c, flush=True)
