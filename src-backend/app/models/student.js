'use strict'

const createGuts = require('../helpers/model-guts')

const name = 'Student'
const tableName = 'Students'

const selectableProps = [
  'id',
  'classId',
  'githubUsername',
  'displayName',
  'createdAt',
]

module.exports = knex => {
  const guts = createGuts({
    knex,
    name,
    tableName,
    selectableProps
  })

  return {
    ...guts,
  }
}