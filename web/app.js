const Server = require('./server.js')
const port = (process.env.PORT || 5250)
const app = Server.app()

app.listen(port)
console.log(`Listening at http://localhost:${port}`)