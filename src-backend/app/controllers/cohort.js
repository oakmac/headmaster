const R = require('ramda')
const { Class, UserClass } = require('../models')
const { isArray } = require('../util')
const { notFoundPage } = require('./errors')

function acceptClassroomInvitation (req, res, nextFn) {
  const userId = req.user.id

  return UserClass.setClassesForUser(userId, [{
    id: req.params.classId
  }])
    .then(function () {
      nextFn()
    })
    .catch(nextFn)
}

// does the :classId route param exist?
function validClassroomParam (req, res, nextFn) {
  const classroomSlug = req.params.classId

  Class.getBySlug(classroomSlug)
    .then(function (classroom) {
      // FIXME: classroom should not be an array here...
      if (isArray(classroom)) {
        if (classroom.length === 1) {
          classroom = classroom[0]
        } else {
          classroom = null
        }
      }

      if (classroom) {
        nextFn()
      } else {
        notFoundPage(req, res)
      }
    })
    .catch(function () {
      // TODO: this should be a server error page; not really a 404?
      notFoundPage(req, res)
    })
}

// does this user have access to view :classId?
function validClassroomPermission (req, res, nextFn) {
  const requestSlug = req.params.classId
  const userId = req.user && req.user.id

  // user is not logged in
  if (!userId) {
    notFoundPage(req, res)
    return
  }

  UserClass.getClassesForUser(userId)
    .then(function (classes) {
      const userClassroomSlugs = R.pluck('slug', classes)
      if (R.includes(requestSlug, userClassroomSlugs)) {
        nextFn()
      } else {
        // NOTE: send 404 here instead of 401 for security reasons
        notFoundPage(req, res)
      }
    })
    .catch(nextFn)
}

module.exports = {
  acceptClassroomInvitation,
  validClassroomParam,
  validClassroomPermission
}
