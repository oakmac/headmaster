'use strict'

const initStudentModel = require('./student')
const initTouchpointModel = require('./touchpoint')
const R = require('ramda')

const createGuts = require('../helpers/model-guts')

const name = 'Class'
const tableName = 'Classes'

const selectableProps = [
  'id',
  'slug',
  'name',
  'createdAt',
]

module.exports = knex => {
  const guts = createGuts({
    knex,
    name,
    tableName,
    selectableProps,
  })

  const Student = initStudentModel(knex)
  const Touchpoint = initTouchpointModel(knex)

  function selectBySlug(classSlug) {
    return knex
      .from(tableName)
      .where({
        slug: classSlug,
      })
  }

  function createAndAssignStudentsToClass(classSlug, arrayOfStudents) {
    return knex.transaction(function(transaction) {
      return selectBySlug(classSlug)
        .transacting(transaction)
        .select('id')
        .then(function( classes ) {
          const classId = R.path([0, 'id'])(classes)
          const studentsToInsert = R.map(
            R.assoc('classId', classId)
          )(arrayOfStudents)

          return Student.bulkCreate(studentsToInsert)
            .transacting(transaction)
        })
    })
  }

  function getStudentsForClass(classSlug) {
    return knex.transaction(function(transaction) {
      return selectBySlug(classSlug)
        .transacting(transaction)
        .select('id')
        .then(function( classes ) {
          const classId = R.path([0, 'id'])(classes)

          return Student.find({
              classId
            })
            .transacting(transaction)
        })
    })
  }

  function getDashboardForClass(classSlug) {

    return knex.transaction(function(transaction) {
      return selectBySlug(classSlug)
        .transacting(transaction)
        .select()
        .then(function(classes) {
          const classInfo = R.path([0])(classes)
          const classId = classInfo.id

          return Student.find({
              classId
            })
            .transacting(transaction)
            .then(function(students) {
              return Promise.all(R.map((student) => (
                  knex.from('StudentsEvents')
                    .where({
                      studentId: student.id,
                    })
                    .leftJoin('Users', 'StudentsEvents.userId', 'Users.id')
                    .select([
                      'StudentsEvents.body',
                      'StudentsEvents.createdAt',
                      'Users.displayName',
                    ])
                    .orderBy('StudentsEvents.createdAt', 'desc')
                    .transacting(transaction)
                    .map(Touchpoint.parseFromSQLite)
                    .then(R.assoc('events', R.__, student))
                  )
                )(students))
                .then(R.assoc('students', R.__, classInfo))
            })
        })
    })
  }

  return {
    ...guts,
    createAndAssignStudentsToClass,
    getStudentsForClass,
    getDashboardForClass,
  }
}