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

const session = require('express-session')
const KnexSessionStore = require('connect-session-knex')(session);
const store = new KnexSessionStore({
  knex,
})

const viewsDir = path.resolve(__dirname, 'views')
const homepageTemplate = slurpFile(path.join(viewsDir, 'homepage.mustache'))
// FIXME: this is a hack
const dashboardTemplate = slurpFile(path.join(__dirname, '../../public/development.html'))

const loginRoutes = require('./routes/login')
const apiRoutes = require('./routes/api')

// -----------------------------------------------------------------------------
// Express Application + Middleware

const app = express()

app.use(express.json())
app.use(express.urlencoded({ extended: false }))

app.use(session({
  secret: 'keyboard cat',
  resave: true,
  saveUninitialized: true,
  store,
}))

app.use(passport.initialize())
app.use(passport.session())

app.use(express.static(PUBLIC_PATH))

app.get('/', homepage)

app.use('/dashboard', ensureLoggedIn('/login/github'))
app.get('/dashboard', dashboard)

// everything past /api requires an authenticated user
app.use('/api', apiAuthentication)

app.use('/', [loginRoutes, apiRoutes])

app.listen(PORT, () => console.log(`Headmaster listening on port ${PORT}!`))

// -----------------------------------------------------------------------------
// Page Endpoints
// TODO: move these elsewhere?

function homepage (req, res) {
  const userLoggedIn = isFn(req.isAuthenticated) && req.isAuthenticated()
  res.send(mustache.render(homepageTemplate, {userLoggedIn: userLoggedIn}))
}

function dashboard (req, res) {
  res.send(mustache.render(dashboardTemplate, {}))
}

function apiAuthentication (req, res, nextFn) {
  if (!req.isAuthenticated || !req.isAuthenticated()) {
    res.status(401).json({
      error: 'user not authorized',
    })
  } else {
    nextFn()
  }
}

// -----------------------------------------------------------------------------
// Utils
// TODO: move this to it's own namespace / module

function slurpFile (filename) {
  return fs.readFileSync(filename, {encoding: 'utf8'})
}

function isFn (f) {
  return typeof f === 'function'
}
