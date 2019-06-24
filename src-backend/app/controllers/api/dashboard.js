const R = require('ramda')
const { mapDashboardSQLToResponse } = require('../../helpers/dashboard')

const {
  refreshStudentsGithubResponses,
} = require('../../helpers/students-github')

const { UserClass } = require('../../models')
const { Class } = require('../../models')

// TODO assuming first class found for user is the class to goto.
// FIX make frontend page for selecting from existing classes for user.
function getDashboardForAuthorizedUser(req, res, next) {
  const userId = req.user.id

  return UserClass.getClassesForUser(userId)
    .then(R.pipe(
      R.head,
      R.pick(['id']),
    ))
    .then(function(classInfo){
      return refreshStudentsGithubResponses(R.prop('id')(classInfo))
        .then(R.always(classInfo))
    })
    .then(Class.getDashboardForClass)
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