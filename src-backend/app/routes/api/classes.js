const {
  getClassesForAuthorizedUser,
} = require('../../controllers/api/classes')

const router = require('express').Router()

router.route('/classes')
  .get(getClassesForAuthorizedUser)

module.exports = router