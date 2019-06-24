const { mapDashboardSQLToResponse } = require('../../helpers/dashboard')

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

function getClassDashboard(req, res, next) {
  const classSlug = req.params.classSlug

  return Class.getDashboardForClass({
      slug: classSlug,
    })
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