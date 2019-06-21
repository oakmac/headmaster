const {
  getTouchpointsForStudent,
  postTouchpointsForStudent,
  } = require('../../controllers/api/students')
  
const router = require('express').Router()

router.route('/students/:studentId/touchpoints')
  .get(getTouchpointsForStudent)
  // example post body:
  // {
  //   "touchpoints": [
  //     {
  //       "tags":[],
  //       "something":""
  //     },
  //     {
  //       "tags":[],
  //       "something":""
  //     },
  //     {
  //       "tags":[],
  //       "something":""
  //     }
  //   ]
  // }
  .post(postTouchpointsForStudent)

module.exports = router