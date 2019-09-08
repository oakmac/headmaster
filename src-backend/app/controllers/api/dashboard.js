const R = require('ramda')
const { mapDashboardSQLToResponse } = require('../../helpers/dashboard')

const {
  refreshStudentsGithubResponses,
} = require('../../helpers/students-github')

const { UserCohort } = require('../../models')
const { Cohort } = require('../../models')

// TODO assuming first cohort found for user is the cohort to goto.
// FIX make frontend page for selecting from existing cohorts for user.
function getDashboardForAuthorizedUser(req, res, next) {
  const userId = req.user.id

  return UserCohort.getCohortsForUser(userId)
    .then(R.pipe(
      R.head,
      R.pick(['id']),
    ))
    // .then(function(cohortInfo){
    //   return refreshStudentsGithubResponses(R.prop('id')(cohortInfo))
    //     .then(R.always(cohortInfo))
    // })
    .then(Cohort.getDashboardForCohort)
    .then(mapDashboardSQLToResponse)
    .then(function(dashboard) {
      res.json(dashboard)
      next()
    })
    .catch(next)
}

module.exports = {
  getDashboardForAuthorizedUser,
}