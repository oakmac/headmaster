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

  function create(props) {
    const body = JSON.stringify(props.body)

    return guts.create({
      ...props,
      body,
    })
  }

  function update(props) {
    const body = JSON.stringify(props.body)

    return guts.update({
      ...props,
      body,
    })
  }

  function find(filters) {
    return guts.find(filters)
      .then(function(results) {
        return R.map(R.evolve({
          body: JSON.parse,
        }))(results)
      })
  }

  // TODO finish JSON.parse on finds, including what get's returned on create

  return {
    ...guts,
    create,
    update,
    find,
  }
}