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

  const stringifyForSQLite = R.evolve({
    body: JSON.stringify,
  })

  const parseFromSQLite = R.evolve({
    body: JSON.parse,
  })

  function create(props) {
    return guts.create(
      stringifyForSQLite(props)
    )
  }

  function bulkCreate(array) {
    const touchpoints = R.map(
      stringifyForSQLite
    )(array)

    return guts
      .bulkCreate(touchpoints)
  }

  function update(props) {
    return guts.update(
      stringifyForSQLite(props)
    )
  }

  function find(filters) {
    return guts.find(filters)
      .then(function(results) {
        return R.map(
          parseFromSQLite
        )(results)
      })
  }

  // TODO finish JSON.parse on finds, including what get's returned on create

  return {
    ...guts,
    create,
    update,
    find,
    bulkCreate,
    // could export outside of init, but TODO for later.
    stringifyForSQLite,
    parseFromSQLite,
  }
}