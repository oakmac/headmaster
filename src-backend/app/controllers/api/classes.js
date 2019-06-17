const { UserClass } = require('../../models')

function getClassesForAuthorizedUser(req, res, next) {
  const userId = req.user.id

  return UserClass.getClassesForUser(userId)
    .then(function(classes) {
      res.json(classes)
      next()
    })
    .catch(next)
}

module.exports = {
  getClassesForAuthorizedUser,
}