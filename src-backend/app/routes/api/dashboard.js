const {
  getDashboardForAuthorizedUser,
} = require('../../controllers/api/dashboard')

const router = require('express').Router()

router.route('/dashboard')
  .get(getDashboardForAuthorizedUser)

module.exports = router