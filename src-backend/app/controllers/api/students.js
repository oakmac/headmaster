const { Touchpoint } = require('../../models')

// TODO make middleware for checking if student is part of user's class.

function getTouchpointsForStudent(req, res, next) {
  const studentId = req.params.studentId

  return Touchpoint.find({
      studentId,
    })
    .then(function(touchpoints) {
      res.json(touchpoints)
      next()
    })
    .catch(next)
}

function postTouchpointForStudent(req, res, next) {
  const userId = req.user.id
  const touchpoints = req.body.touchpoints
  const studentId = req.params.studentId

  return Touchpoint.bulkCreate(
    R.map(
      (body) => ({
        body,
        userId,
        studentId,
      })
    )(touchpoints))
    .then(function(createdTouchpoint) {
      res.json({
        success: 'touchpoint added',
        touchpoint: createdTouchpoint,
      })
      next()
    })
    .catch(next)
}

module.exports = {
  getTouchpointsForStudent,
  postTouchpointForStudent,
}