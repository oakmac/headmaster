const { UserCohort } = require('../models')
const mustache = require('mustache')

const { isFn, loadTemplate } = require('../util')

const router = require('express').Router()

const homepageTemplate = loadTemplate('homepage')

// add an array of cohorts for this user to req.cohorts
// sets req.cohorts to null if they are not logged in
function getCohortsForUser (req, res, nextFn) {
  // grab cohorts if the user is logged in
  if (isFn(req.isAuthenticated) && req.isAuthenticated() && req.user && req.user.id) {
    UserCohort.getCohortsForUser(req.user.id)
      .then(function (cohorts) {
        req.cohorts = cohorts
        nextFn()
      })
      .catch(nextFn)
  } else {
    // do nothing if they are not logged in
    req.cohortSlugs = null
    nextFn()
  }
}

function homepage (req, res, nextFn) {
  const userLoggedIn = isFn(req.isAuthenticated) && req.isAuthenticated()

  res.send(mustache.render(homepageTemplate, {
    cohorts: req.cohorts,
    userLoggedIn: userLoggedIn,
  }))
}

router.route('/').get([getCohortsForUser, homepage])

module.exports = router
