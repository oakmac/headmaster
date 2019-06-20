
const passport = require('passport')
const configurePassport = require('./configure-passport')
const router = require('express').Router()

configurePassport()

router.route('/login')
  .get(function(req, res){
    res.send('Welcome to Headmaster.')
  })

router.route('/login/github')
  .get(passport.authenticate('github'))

router.route('/login/github/callback')
  .get(
    passport.authenticate(
      'github',
      {
        failureRedirect: '/login',
      }
    ),
    function(req, res) {
      res.redirect('/dashboard')
    }
  )

router.route('/logout')
  .get(function(req, res){
    req.logout()
    res.redirect('/login')
  })

module.exports = router