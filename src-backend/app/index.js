const path = require('path')

const express = require('express')
const passport = require('passport')
const { ensureLoggedIn } = require('connect-ensure-login')

const { loadEnvironmentVariables } = require('../utils/handle-config')

loadEnvironmentVariables()

const PORT = process.env.PORT || 3000
const PUBLIC_PATH = path.resolve(__dirname, '../../public')

const app = express()

app.use(express.json())
app.use(express.urlencoded({ extended: false }))
app.use(require('cookie-parser')())
app.use(require('express-session')({ secret: 'keyboard cat', resave: true, saveUninitialized: true }))
app.use(passport.initialize())
app.use(passport.session())

app.use('/dashboard', ensureLoggedIn('/login'))
app.use('/dashboard', express.static(PUBLIC_PATH))

app.use('/', [
  require('./routes/login'),
// require('./routes/user_routes'),
// require('./routes/project_routes')
])
// app.get('/login', (req, res) => res.send('Hello World!'))

app.listen(PORT, () => console.log(`Example app listening on port ${PORT}!`))
