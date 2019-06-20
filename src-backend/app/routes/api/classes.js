const {
  getClassesForAuthorizedUser,
  setClassesForAuthorizedUser,
} = require('../../controllers/api/classes')

const router = require('express').Router()

router.route('/classes')
  .get(getClassesForAuthorizedUser)
  .post(setClassesForAuthorizedUser)

module.exports = router