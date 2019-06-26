const path = require('path')
const mustache = require('mustache')
const knex = require('../config/database')

const express = require('express')
const passport = require('passport')
const { ensureLoggedIn } = require('connect-ensure-login')

const { loadEnvironmentVariables } = require('../utils/handle-config')

const { isFn, loadTemplate } = require('./util')

loadEnvironmentVariables()

const PORT = process.env.PORT || 3000
const PUBLIC_PATH = path.resolve(__dirname, '../../public')

const session = require('express-session')
const KnexSessionStore = require('connect-session-knex')(session);
const store = new KnexSessionStore({
  knex,
})

const homepageTemplate = loadTemplate('homepage')

const loginRoutes = require('./routes/login')
const apiRoutes = require('./routes/api')
const classroomRoute = require('./routes/classroom')

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

// everything past /api requires an authenticated user
app.use('/api', apiAuthentication)

app.use('/', [loginRoutes, apiRoutes, classroomRoute])

app.listen(PORT, () => console.log(`Headmaster listening on port ${PORT}!`))

// -----------------------------------------------------------------------------
// Page Endpoints
// TODO: move these elsewhere?

function homepage (req, res) {
  const userLoggedIn = isFn(req.isAuthenticated) && req.isAuthenticated()
  res.send(mustache.render(homepageTemplate, {userLoggedIn: userLoggedIn}))
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
