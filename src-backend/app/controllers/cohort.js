const R = require('ramda')
const { Cohort, UserCohort } = require('../models')
const { notFoundPage } = require('./errors')

function acceptCohortInvitation (req, res, nextFn) {
  const userId = req.user.id
  const cohortSlug = req.params.cohortSlug

  return Cohort.getBySlug(cohortSlug)
    .then(function(cohort) {
      return UserCohort.setCohortsForUser(userId, [{
        id: cohort.id,
      }])
    })
    .then(function () {
      nextFn()
    })
    .catch(nextFn)
}

function validCohortParam (req, res, nextFn) {
  const cohortSlug = req.params.cohortSlug

  return Cohort.getBySlug(cohortSlug)
    .then(function (cohort) {
      if (cohort) {
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
function validCohortPermission (req, res, nextFn) {
  const requestSlug = req.params.cohortSlug
  const userId = req.user && req.user.id

  // user is not logged in
  if (!userId) {
    notFoundPage(req, res)
    return
  }

  return UserCohort.getCohortsForUser(userId)
    .then(function (cohorts) {
      const usercohortSlugs = R.pluck('slug', cohorts)
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
  acceptCohortInvitation,
  validCohortParam,
  validCohortPermission
}
