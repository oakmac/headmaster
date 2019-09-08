'use strict'

const R = require('ramda')

const createGuts = require('../helpers/model-guts')

const name = 'Student'
const tableName = 'Students'

const selectableProps = [
  'id',
  'cohortId',
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

  function findWithGithub(filter) {
    return knex.from(tableName)
      .leftJoin('StudentsGithubResponses', 'StudentsGithubResponses.studentId', 'Students.id')
      .select({
        id: 'Students.id',
        cohortId: 'Students.cohortId',
        githubUsername: 'Students.githubUsername',
        displayName: 'Students.displayName',
        githubActivityResponse: 'StudentsGithubResponses.body',
      })
      .where(filter)
  }

  return {
    ...guts,
    findWithGithub,
  }
}