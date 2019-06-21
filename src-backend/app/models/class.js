'use strict'

const initStudentModel = require('./student')
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

  return {
    ...guts,
    createAndAssignStudentsToClass,
    getStudentsForClass,
  }
}