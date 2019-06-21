const {
  getClassesForAuthorizedUser,
  setClassesForAuthorizedUser,
  setStudentsForClass,
  getStudentsForClass,
} = require('../../controllers/api/classes')

const router = require('express').Router()

router.route('/classes')
  .get(getClassesForAuthorizedUser)
  .post(setClassesForAuthorizedUser)

router.route('/classes/:classSlug/students')
  .get(getStudentsForClass)
  .post(setStudentsForClass)

module.exports = router