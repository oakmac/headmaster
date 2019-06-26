const { UserClass } = require('../models')

function acceptClassroomInvitation(req, res, nextFn) {
  const userId = req.user.id

  return UserClass.setClassesForUser(userId, [{
      id: req.params.classId,
    }])
    .then(function() {
      nextFn()
    })
    .catch(nextFn)
}


module.exports = {
  acceptClassroomInvitation,
}