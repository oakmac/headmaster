'use strict'
const R = require('ramda')

const createGuts = require('../helpers/model-guts')

const name = 'UserClass'
const tableName = 'UsersClasses'

const selectableProps = [
  'id',
  'userId',
  'classId',
  'role',
  'createdAt',
]

module.exports = knex => {
  const guts = createGuts({
    knex,
    name,
    tableName,
    selectableProps
  })

  function setClassesForUser(userId, classes) {

  }

  function getClassesForUser(userId) {
    return knex.select()
      .from(tableName)
      .leftJoin('Classes', `${tableName}.classId`, 'Classes.id')
      .where({
        userId,
      })
  }

  return {
    ...guts,
    getClassesForUser,
  }
}