const path = require('path')
const mustache = require('mustache')

const { isFn, loadTemplate } = require('../../util')

const { acceptClassroomInvitation, validClassroomParam, validClassroomPermission } = require('../../controllers/classroom')

const router = require('express').Router()

const classroomTemplate = loadTemplate('classroom')
const invitationPageTempate = loadTemplate('classroom.invitation')

function invitationPage (req, res, nextFn) {
  const userLoggedIn = isFn(req.isAuthenticated) && req.isAuthenticated()
  const classId = req.params.classId

  res.send(mustache.render(invitationPageTempate, {
    userLoggedIn,
    classId,
  }))

  nextFn()
}

// TODO: does it make sense to call a redirect and then nextFn() directly after?
function acceptPage (req, res, nextFn) {
  res.redirect('/dashboard')
  nextFn()
}

function classroomPage (req, res, nextFn) {
  res.send(mustache.render(classroomTemplate, {}))
}

router.route('/classroom/:classId').get([validClassroomParam, validClassroomPermission, classroomPage])

router.route('/classroom/:classId/invitation').get([validClassroomParam, invitationPage])

router.route('/classroom/:classId/invitation/accept')
  .get([
    validClassroomParam,
    acceptClassroomInvitation,
    acceptPage,
  ])

module.exports = router
