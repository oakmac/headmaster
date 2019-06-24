const R = require('ramda')
const {
  distanceInWords,
  format,
} = require('date-fns')

const { UserClass } = require('../../models')
const { Class } = require('../../models')

function getClassesForAuthorizedUser(req, res, next) {
  const userId = req.user.id

  return UserClass.getClassesForUser(userId)
    .then(function(classes) {
      res.json(classes)
      next()
    })
    .catch(next)
}

function setClassesForAuthorizedUser(req, res, next) {
  const userId = req.user.id

  return UserClass.setClassesForUser(userId, req.body.classes)
    .then(function(classes) {
      res.json(classes)
      next()
    })
    .catch(next)
}

function setStudentsForClass(req, res, next) {
  const classSlug = req.params.classSlug

  return Class.createAndAssignStudentsToClass(classSlug, req.body.students)
    .then(function() {
      // TODO: actually return students and their ids
      res.json({
        success: 'students added',
      })
      next()
    })
    .catch(next)
}

function getStudentsForClass(req, res, next) {
  const classSlug = req.params.classSlug

  return Class.getStudentsForClass(classSlug)
    .then(function(students) {
      res.json(students)
      next()
    })
    .catch(next)
}


// TODO some of these transforms should move to another file probs.
// START transforms
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
// END transforms

function getClassDashboard(req, res, next) {
  const classSlug = req.params.classSlug

  return Class.getDashboardForClass(classSlug)
    .then(mapDashboardSQLToResponse)
    .then(function(dashboard) {
      res.json(dashboard)
      next()
    })
    .catch(next)
}

module.exports = {
  getClassesForAuthorizedUser,
  setClassesForAuthorizedUser,
  setStudentsForClass,
  getStudentsForClass,
  getClassDashboard,
}