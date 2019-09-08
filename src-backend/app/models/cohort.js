'use strict'

const initStudentModel = require('./student')
const R = require('ramda')

const createGuts = require('../helpers/model-guts')

const name = 'Cohort'
const tableName = 'Cohorts'

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

  function _getBySlug(cohortSlug) {
    return knex
      .from(tableName)
      .where({
        slug: cohortSlug,
      })
  }

  const getBySlug = R.pipe(
    _getBySlug,
    R.then(R.head),
  )

  function createAndAssignStudentsToCohort(cohortSlug, arrayOfStudents) {
    return knex.transaction(function(transaction) {
      return _getBySlug(cohortSlug)
        .transacting(transaction)
        .select('id')
        .then(function( cohorts ) {
          const cohortId = R.path([0, 'id'])(cohorts)
          const studentsToInsert = R.map(
            R.assoc('cohortId', cohortId)
          )(arrayOfStudents)

          return Student.bulkCreate(studentsToInsert)
            .transacting(transaction)
        })
    })
  }

  function getStudentsForCohort(cohortSlug) {
    return knex.transaction(function(transaction) {
      return _getBySlug(cohortSlug)
        .transacting(transaction)
        .select('id')
        .then(function( cohorts ) {
          const cohortId = R.path([0, 'id'])(cohorts)

          return Student.find({
              cohortId
            })
            .transacting(transaction)
        })
    })
  }

  function getDashboardForCohort(filter) {

    return knex.transaction(function(transaction) {
      return knex.from(tableName)
        .where(filter)
        .transacting(transaction)
        .select()
        .then(function(cohorts) {
          const cohortInfo = R.path([0])(cohorts)
          const cohortId = cohortInfo.id

          return Student.findWithGithub({
              cohortId
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
                    .then(R.assoc('events', R.__, student))
                  )
                )(students))
                .then(R.assoc('students', R.__, cohortInfo))
            })
        })
    })
  }

  return {
    ...guts,
    createAndAssignStudentsToCohort,
    getBySlug,
    getDashboardForCohort,
    getStudentsForCohort,
  }
}
