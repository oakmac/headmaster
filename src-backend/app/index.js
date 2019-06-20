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

// should send error, move to middlewares later
app.use('/api', function(req, res, next) {
  if (!req.isAuthenticated || !req.isAuthenticated()) {
    res.status(401).json({
      error: 'user not authorized',
    })
  }
  next()
})

app.use('/', [
  require('./routes/login'),
  require('./routes/api'),
])

app.listen(PORT, () => console.log(`Headmaster listening on port ${PORT}!`))
