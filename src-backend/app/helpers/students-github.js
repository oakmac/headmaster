const R = require('ramda')

const {
  getActivityForUser,
  transformGitHubResponse,
} = require('./github')

const {
  StudentGithubResponse,
} = require('../models')


function refreshStudentsGithubResponses(cohortId) {
  return StudentGithubResponse
    .getStaleStudents(cohortId)
    .then(function (staleStudents) {
      return Promise.all(R.map(
        (student) => {
          return getActivityForUser(student.githubUsername)
            .then(transformGitHubResponse)
            .then((responseToCache) => {
              return {
                studentId: student.studentId,
                body: responseToCache,
              }
            })

        }
      )(staleStudents))
    })
    .then((responses) => {
      if (R.isEmpty(responses)) {
        return
      }
      return StudentGithubResponse.bulkCreate(responses)
    })
}

module.exports = {
  refreshStudentsGithubResponses,
}