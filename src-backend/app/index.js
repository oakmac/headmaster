const path = require('path')
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

const homepageRoute = require('./routes/homepage')
const loginRoutes = require('./routes/login')
const apiRoutes = require('./routes/api')
const cohortRoutes = require('./routes/cohort')

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

app.use('/', [homepageRoute, loginRoutes, apiRoutes, cohortRoutes])

// everything past /api requires an authenticated user
app.use('/api', apiAuthentication)

app.listen(PORT, () => console.log(`Headmaster listening on port ${PORT}!`))

// -----------------------------------------------------------------------------
// Page Endpoints
// TODO: move these elsewhere?

function apiAuthentication (req, res, nextFn) {
  if (!req.isAuthenticated || !req.isAuthenticated()) {
    res.status(401).json({
      error: 'user not authorized',
    })
  } else {
    nextFn()
  }
}
