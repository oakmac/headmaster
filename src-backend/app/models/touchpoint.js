'use strict'

const createGuts = require('../helpers/model-guts')
const R = require('ramda')

const name = 'Touchpoint'
const tableName = 'StudentsEvents'

const selectableProps = [
  'id',
  'studentId',
  'userId',
  'body',
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