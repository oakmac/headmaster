'use strict'

const createGuts = require('../helpers/model-guts')

const name = 'User'
const tableName = 'Users'

// Properties that are allowed to be selected from the database for reading.
// (e.g., `password` is not included and thus cannot be selected)
const selectableProps = [
  'id',
  'email',
  'githubId',
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