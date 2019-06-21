const {
  getClassesForAuthorizedUser,
  setClassesForAuthorizedUser,
  setStudentsForClass,
  getStudentsForClass,
} = require('../../controllers/api/classes')

const router = require('express').Router()

router.route('/classes')
  .get(getClassesForAuthorizedUser)
  // example post body:
  // {
  //    "classes": [{
  //      "slug": "test-103",
  //      "name": "Test Class with Demo Students"
  //    }]
  // }
  .post(setClassesForAuthorizedUser)

router.route('/classes/:classSlug/students')
  .get(getStudentsForClass)
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
  .post(setStudentsForClass)

module.exports = router