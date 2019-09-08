const {
  getCohortsForAuthorizedUser,
  setCohortsForAuthorizedUser,
  setStudentsForCohort,
  getStudentsForCohort,
  getCohortDashboard,
} = require('../../controllers/api/cohort')

const router = require('express').Router()

router.route('/cohorts')
  .get(getCohortsForAuthorizedUser)
  // example post body:
  // {
  //    "cohorts": [{
  //      "slug": "test-103",
  //      "name": "Test Cohort with Demo Students"
  //    }]
  // }
  .post(setCohortsForAuthorizedUser)

router.route('/cohorts/:cohortSlug/students')
  .get(getStudentsForCohort)
  // example post body:
  // {
  //   "students": [
  //     {
  //       "displayName":"Rich Hickey",
  //       "githubUsername":"richhickey"
  //     },
  //     {
  //       "displayName":"Jeremy Ashkenas",
  //       "githubUsername":"jashkenas"
  //     },
  //     {
  //       "displayName":"David Nolen",
  //       "githubUsername":"swannodette"
  //     }
  //   ]
  // }
  .post(setStudentsForCohort)

router.route('/cohorts/:cohortSlug/touchpoints')
  .get(getCohortDashboard)

module.exports = router