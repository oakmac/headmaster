const {
  Touchpoint,
  Student,
} = require('../../models')
const R = require('ramda')

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

function postTouchpointsForStudent(req, res, next) {
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

function updateStudent(req, res, nextFn) {
  const studentId = req.params.studentId

  return Student.update(studentId, req.body)
    .then(function() {
      res.json({
        success: 'student updated',
      })
      nextFn()
    })
    .catch(nextFn)
}

module.exports = {
  getTouchpointsForStudent,
  postTouchpointsForStudent,
  updateStudent,
}