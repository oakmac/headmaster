'use strict'

const createGuts = require('./model-guts')
const R = require('ramda')

module.exports = ({
    knex = {},
    name = 'name',
    tableName = 'tablename',
    selectableProps = [],
    jsonProps = ['body'],
    timeout = 1000,
  }) => {
    const guts = createGuts({
      knex,
      name,
      tableName,
      selectableProps
    })

    const stringifyForSQLite = R.evolve(R.zipObj(
      jsonProps,
      R.repeat(JSON.stringify, jsonProps.length),
    ))

    const parseFromSQLite = R.evolve(R.zipObj(
      jsonProps,
      R.repeat(JSON.parse, jsonProps.length),
    ))

    function create(props) {
      return guts.create(
        stringifyForSQLite(props)
      )
    }

    function bulkCreate(array) {
      return guts
        .bulkCreate(R.map(
          stringifyForSQLite
        )(array))
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