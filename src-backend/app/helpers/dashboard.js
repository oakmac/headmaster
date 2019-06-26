const R = require('ramda')
const {
  distanceInWords,
  format,
} = require('date-fns')
const {
  filterForEventsByType,
} = require('./github')


const getTimeAgo = (time) => (
  distanceInWords( new Date(), time, { addSuffix: true })
)

const transformEvent = R.converge(
  R.merge, [
    R.prop('body'),
    R.applySpec({
      timeAgo: R.pipe(
        R.prop('createdAt'),
        getTimeAgo,
      ),
      recordedBy: R.prop('displayName'),
    }),
  ],
)

const transformGithubData = R.pipe(
  R.evolve({
    githubActivityResponse: R.pipe(
      JSON.parse,
      R.prop('data'),
    ),
  }),
  R.applySpec({
    avatar: R.pipe(
      R.prop('githubActivityResponse'),
      R.head,
      R.path(['actor', 'avatar_url']),
    ),
    lastGithubCommit: R.pipe(
      R.prop('githubActivityResponse'),
      R.head,
      R.prop('created_at'),
    ),
    githubActivityResponse: R.pipe(
      R.prop('githubActivityResponse'),
      filterForEventsByType,
      R.groupBy(R.pipe(
        R.prop('created_at'),
        // group by year, week of year, and day of week.
        R.partialRight(format, ['YYYY-WW-d']),
      )),
    ),
  })
)

const getMostRecentTouchpointProp = (prop) => R.pipe(
  R.prop('events'),
  R.find(R.pipe(
    R.path(['body', prop]),
    R.isNil,
    R.not,
  )),
  R.path(['body', prop]),
)

// TODO parsing is slow github json, consider changing to jsonb, or filtering and omitting properties before caching
const transformStudent = R.pipe(
  R.omit([
    'id',
    'classId',
    'createdAt',
  ]),
  R.converge(
    R.merge, [
      R.applySpec({
        name: R.prop('displayName'),
        shortName: R.pipe(
          R.prop('displayName'),
          R.split(' '),
          R.head,
        ),
        github: R.prop('githubUsername'),
        stoplight: getMostRecentTouchpointProp('stoplight'),
        events: R.pipe(
          R.prop('events'),
          R.map(transformEvent),
        ),
      }),
      R.ifElse(
        R.pipe(
          R.prop('githubActivityResponse'),
          R.isNil,
        ),
        R.always({}),
        transformGithubData,
      ),
    ]
  )
)

function mapDashboardSQLToResponse(dashboard) {
  return R.applySpec({
    id: R.prop('slug'),
    title: R.prop('name'),
    // TODO making some assumptions about start date,
    // add actual start and end date for classes
    subtitle: R.pipe(
      R.prop('createdAt'),
      R.partialRight(format, ['MMM YYYY']),
    ),
    students: R.pipe(
      R.prop('students'),
      R.indexBy(R.prop('id')),
      R.map(transformStudent),
    ),
  })(dashboard)
}

module.exports = {
  getTimeAgo,
  transformEvent,
  getMostRecentTouchpointProp,
  transformStudent,
  transformEvent,
  mapDashboardSQLToResponse,
}