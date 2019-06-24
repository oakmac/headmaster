const path = require('path')
const fs = require('fs-plus')
const mustache = require('mustache')
const knex = require('../config/database')

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

const session = require('express-session')
const KnexSessionStore = require('connect-session-knex')(session);
const store = new KnexSessionStore({
  knex,
})

app.use(session({
  secret: 'keyboard cat',
  resave: true,
  saveUninitialized: true,
  store,
}))

app.use(passport.initialize())
app.use(passport.session())

app.use(express.static(PUBLIC_PATH))

const viewsDir = path.resolve(__dirname, 'views')
const homepageTemplate = slurpFile(path.join(viewsDir, 'homepage.mustache'))

function homepage (req, res) {
  res.send(mustache.render(homepageTemplate, {}))
}

app.get('/', homepage)

app.use('/dashboard', ensureLoggedIn('/login/github'))
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

// -----------------------------------------------------------------------------
// Utils
// TODO: move this to it's own namespace / module

function slurpFile (filename) {
  return fs.readFileSync(filename, {encoding: 'utf8'})
}
