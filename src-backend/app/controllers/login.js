const { User } = require('../models')

function loginWithGithub(githubProfile) {
  return User.findOne({
    githubId: githubProfile.id,
  })
  .catch(function (error) {
    // no user with the selected github profile exists yet.  Create this user.
    const userInfo = {
      githubId: githubProfile.id,
      githubUsername: githubProfile.username,
      displayName: githubProfile.displayName,
    }

    return User.create(userInfo)
  })
}

function getUserFromSession(userId) {
  return User.findOne({
    id: userId,
  })
}

module.exports = {
  loginWithGithub,
  getUserFromSession,
}