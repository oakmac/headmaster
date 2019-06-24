'use strict'
const {
  subDays
} = require('date-fns')
const R = require('ramda')

const createGuts = require('../helpers/model-guts')

const name = 'StudentGithubResponse'
const tableName = 'StudentsGithubResponses'

const selectableProps = [
  'id',
  'studentId',
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

  // TODO need to just make this common guts.
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

  function getLatestResponse() {
    return knex.from(tableName)
      .groupBy('studentId')
      .orderBy('createdAt', 'desc')
      .whereNotNull('body')
  } 

  // TODO make more flexible in time units since
  function getStaleStudents(classId, daysSince = 1) {
    return  knex.from('Students')
      .leftJoin(
        getLatestResponse()
          .select(knex.raw(`max(${tableName}.createdAt) as createdAt, studentId, body`))
          .as('latest')
        ,
        'Students.id', `latest.studentId`
      )
      .where('Students.classId', classId)
      .andWhere(function(){
        this
          .where('latest.createdAt', '<', subDays(new Date(), daysSince))
          .orWhereNull('latest.createdAt')
      })
      .select({
        studentId: 'Students.id',
        githubUsername: 'Students.githubUsername',
      })
  }

  return {
    ...guts,
    create,
    update,
    find,
    bulkCreate,
    // could export outside of init, but TODO for later.
    stringifyForSQLite,
    parseFromSQLite,
    getStaleStudents,
  }
}