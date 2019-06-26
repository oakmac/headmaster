const path = require('path')
const mustache = require('mustache')

const { isFn, slurpFile } = require('../../util')

const { acceptClassroomInvitation } = require('../../controllers/classroom')

const router = require('express').Router()

const viewsDir = path.resolve(__dirname, '../../views')
const invitationPageTempate = slurpFile(path.join(viewsDir, 'classroom.invitation.mustache'))

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

router.route('/classroom/:classId/invitation')
  .get(invitationPage)

router.route('/classroom/:classId/invitation/accept')
  .get([
    acceptClassroomInvitation,
    acceptPage,
  ])

module.exports = router
