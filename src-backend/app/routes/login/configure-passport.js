const passport = require('passport')
const GitHubStrategy = require('passport-github').Strategy

const {
  loginWithGithub, getUserFromSession,
} = require('../../controllers/login')

function configurePassport() {
  passport.use(new GitHubStrategy({
      clientID: process.env.GITHUB_CLIENT_ID,
      clientSecret: process.env.GITHUB_CLIENT_SECRET,
      callbackURL: "http://localhost:3000/login/github/callback"
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
