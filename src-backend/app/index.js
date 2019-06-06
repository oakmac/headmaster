const express = require('express')
const path = require('path')


const PORT = process.env.PORT || 3000
const PUBLIC_PATH = path.resolve(__dirname, '../../public')

const app = express()

app.use(express.static(PUBLIC_PATH))

app.get('/login', (req, res) => res.send('Hello World!'))

app.listen(PORT, () => console.log(`Example app listening on port ${PORT}!`))
