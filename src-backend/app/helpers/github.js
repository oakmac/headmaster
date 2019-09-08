const Octokit = require('@octokit/rest')
const R = require('ramda')

const octokit = Octokit({
  auth: {
    clientId: process.env.GITHUB_CLIENT_ID,
    clientSecret: process.env.GITHUB_CLIENT_SECRET,
  },
  // TODO also should be a constant or environment var.
  userAgent: 'Headmaster Dev v0.0.1',
})

const getActivityForUser = R.pipe(
  R.objOf('username'),
  R.assoc('per_page', 100),
  octokit.activity.listPublicEventsForUser,
)

function getActivityForUsers(githubUsernames) {
  return Promise.all(R.map(
    getActivityForUser
  )(githubUsernames))
}

const transformGitHubResponse = R.pipe(
  R.evolve({
    headers: R.pick([
      'date', 'etag', 'last-modified',
    ]),
  }),
  R.omit('status'),
)

function filterForEventsByType(githubEvents, eventTypes = ['PushEvent']) {
  return R.filter(
    R.propSatisfies(
      R.includes(R.__, eventTypes),
    'type')
  )(githubEvents)
}

module.exports = {
  octokit,
  getActivityForUser,
  getActivityForUsers,
  transformGitHubResponse,
  filterForEventsByType,
}