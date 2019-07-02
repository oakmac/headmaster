const path = require('path')
const mustache = require('mustache')

const { isFn, loadTemplate } = require('../util')

const { permissionRequired } = require('../controllers/errors')
const { acceptClassroomInvitation, validClassroomParam, validClassroomPermission } = require('../controllers/cohort')

const router = require('express').Router()

const cohortTemplate = loadTemplate('cohort')
const invitationPageTempate = loadTemplate('cohort.invitation')

function invitationPage (req, res, nextFn) {
  const userLoggedIn = isFn(req.isAuthenticated) && req.isAuthenticated()
  const cohortSlug = req.params.cohortSlug

  res.send(mustache.render(invitationPageTempate, {
    userLoggedIn,
    cohortSlug,
  }))

  nextFn()
}

// TODO: does it make sense to call a redirect and then nextFn() directly after?
function acceptPage (req, res, nextFn) {
  res.redirect('/')
  nextFn()
}

function cohortPage (req, res, _nextFn) {
  res.send(mustache.render(cohortTemplate, {}))
}

function newCohortPage (req, res, _nextFn) {
  res.send('<h1>TODO: write new cohort page :)</h1>')
}

router.route('/new-cohort').get([permissionRequired, newCohortPage])

router.route('/cohort/:cohortSlug').get([validClassroomParam, validClassroomPermission, cohortPage])

router.route('/cohort/:cohortSlug/invitation').get([validClassroomParam, invitationPage])

router.route('/cohort/:cohortSlug/invitation/accept')
  .get([
    validClassroomParam,
    acceptClassroomInvitation,
    acceptPage,
  ])

module.exports = router
