'use strict'

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
    selectableProps
  })

  return {
    ...guts,
  }
}