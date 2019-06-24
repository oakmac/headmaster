const R = require('ramda')
const {
  distanceInWords,
  format,
} = require('date-fns')

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

const getMostRecentTouchpointProp = (prop) => (R.pipe(
  R.prop('events'),
  R.find(R.hasPath(['body', prop])),
  R.path(['body', prop]),
))

const transformStudent = R.pipe(
  R.omit([
    'id',
    'classId',
    'createdAt',
  ]),
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