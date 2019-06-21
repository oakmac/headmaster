const {
  getTouchpointsForStudent,
  postTouchpointForStudent,
  } = require('../../controllers/api/students')
  
const router = require('express').Router()

router.route('/students/:studentId/touchpoints')
  .get(getTouchpointsForStudent)
  .post(postTouchpointForStudent)

module.exports = router