const {
  getTouchpointsForStudent,
  postTouchpointsForStudent,
  updateStudent,
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


router.route('/students/:studentId')
  .put(updateStudent)

module.exports = router