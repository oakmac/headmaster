const passport = require('passport')
const GitHubStrategy = require('passport-github').Strategy

const {
  loginWithGithub, getUserFromSession,
} = require('../../controllers/login')

function configurePassport() {

  if (!(
    process.env.GITHUB_CLIENT_ID &&
    process.env.GITHUB_CLIENT_SECRET
  )) {
    console.error(`
      GITHUB_CLIENT_ID and/or GITHUB_CLIENT_SECRET missing.
      Please confiure in src-backend/config or run server
      with environment variables.
      Server will run, but login with not work.
    `)
    return
  }


  passport.use(new GitHubStrategy({
      clientID: process.env.GITHUB_CLIENT_ID,
      clientSecret: process.env.GITHUB_CLIENT_SECRET,
      callbackURL: `${process.env.HOST}/login/github/callback`
    },
    function(accessToken, refreshToken, profile, cb) {
      loginWithGithub(profile)
        .then(function (user) {
          return cb(null, user)
        })
    }
  ))

  passport.serializeUser(function(user, cb) {
    cb(null, user.id)
  })

  passport.deserializeUser(function(userId, cb) {
    getUserFromSession(userId)
      .then(function(user) {
        cb(null, user)
      })
  })
}

module.exports = configurePassport
