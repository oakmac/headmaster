const passport = require('passport')
const configurePassport = require('./configure-passport')
const router = require('express').Router()

function handleMissingGitHubStrategy(req, res, next) {
  if (passport._strategies.github) {
    next()
    return
  }
  res.status(404).json({
    error: 'GitHub strategy is missing. Set up GITHUB_CLIENT_ID and GITHUB_CLIENT_SECRET and try again.'
  })
}

configurePassport()

router.route('/login/github')
  .get(handleMissingGitHubStrategy)
router.route('/login/github/callback')
  .get(handleMissingGitHubStrategy)

router.route('/login/github')
  .get(passport.authenticate('github'))

router.route('/login/github/callback')
  .get(
    passport.authenticate(
      'github',
      {
        // TODO: we should send them to a "failed to login" page here
        failureRedirect: '/login',
      }
    ),
    function(req, res) {
      res.redirect('/')
    }
  )

router.route('/logout')
  .get(function(req, res){
    req.logout()
    res.redirect('/')
  })

module.exports = router
