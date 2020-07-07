const { Xroom } = require('./xroom')

const api = new Xroom('YOUR_USERNAME', 'YOUR_SECRET')

api.call('room', 'init', {
  id: 'xroom.app/hello-world'
})
  .then(result => console.log(result))
  .catch(error => console.log('API error', error))
