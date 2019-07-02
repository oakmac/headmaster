const R = require('ramda')
const { Class, UserClass } = require('../models')
const { notFoundPage } = require('./errors')

function acceptClassroomInvitation (req, res, nextFn) {
  const userId = req.user.id
  const cohortSlug = req.params.cohortSlug

  return Class.getBySlug(cohortSlug)
    .then(function(classroom) {
      return UserClass.setClassesForUser(userId, [{
        id: classroom.id,
      }])
    })
    .then(function () {
      nextFn()
    })
    .catch(nextFn)
}

function validClassroomParam (req, res, nextFn) {
  const cohortSlug = req.params.cohortSlug

  return Class.getBySlug(cohortSlug)
    .then(function (classroom) {
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

// does this user have access to view :cohortSlug?
function validClassroomPermission (req, res, nextFn) {
  const requestSlug = req.params.cohortSlug
  const userId = req.user && req.user.id

  // user is not logged in
  if (!userId) {
    notFoundPage(req, res)
    return
  }

  return UserClass.getClassesForUser(userId)
    .then(function (classes) {
      const usercohortSlugs = R.pluck('slug', classes)
      if (R.includes(requestSlug, usercohortSlugs)) {
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
