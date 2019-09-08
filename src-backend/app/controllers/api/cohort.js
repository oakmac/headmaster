const { mapDashboardSQLToResponse } = require('../../helpers/dashboard')

const { UserCohort } = require('../../models')
const { Cohort } = require('../../models')

function getCohortsForAuthorizedUser(req, res, next) {
  const userId = req.user.id

  return UserCohort.getCohortsForUser(userId)
    .then(function(cohorts) {
      res.json(cohorts)
      next()
    })
    .catch(next)
}

function setCohortsForAuthorizedUser(req, res, next) {
  const userId = req.user.id

  return UserCohort.setCohortsForUser(userId, req.body.cohorts)
    .then(function(cohorts) {
      res.json(cohorts)
      next()
    })
    .catch(next)
}

function setStudentsForCohort(req, res, next) {
  const cohortSlug = req.params.cohortSlug

  return Cohort.createAndAssignStudentsToCohort(cohortSlug, req.body.students)
    .then(function() {
      // TODO: actually return students and their ids
      res.json({
        success: 'students added',
      })
      next()
    })
    .catch(next)
}

function getStudentsForCohort(req, res, next) {
  const cohortSlug = req.params.cohortSlug

  return Cohort.getStudentsForCohort(cohortSlug)
    .then(function(students) {
      res.json(students)
      next()
    })
    .catch(next)
}

function getCohortDashboard(req, res, next) {
  const cohortSlug = req.params.cohortSlug

  return Cohort.getDashboardForCohort({
      slug: cohortSlug,
    })
    .then(mapDashboardSQLToResponse)
    .then(function(dashboard) {
      res.json(dashboard)
      next()
    })
    .catch(next)
}

module.exports = {
  getCohortsForAuthorizedUser,
  setCohortsForAuthorizedUser,
  setStudentsForCohort,
  getStudentsForCohort,
  getCohortDashboard,
}