'use strict'
const {
  subDays
} = require('date-fns')
const R = require('ramda')

const createGuts = require('../helpers/model-guts-with-json')

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

  function getLatestResponse() {
    return knex
      .select([
        knex.raw(`max("${tableName}"."createdAt") AS "latestResponse"`),
        'studentId',
      ])
      .from(tableName)
      .as('latest')
      .groupBy('studentId')
      .whereNotNull('body')
  } 

  // TODO make more flexible in time units since
  function getStaleStudents(classId, daysSince = 1) {
    return  knex.from('Students')
      .leftJoin(
        getLatestResponse(),
        'Students.id', `latest.studentId`
      )
      .where('Students.classId', classId)
      .andWhere(function(){
        this
          .where('latest.latestResponse', '<', subDays(new Date(), daysSince))
          .orWhereNull('latest.latestResponse')
      })
      .whereNotNull('Students.githubUsername')
      .select({
        studentId: 'Students.id',
        githubUsername: 'Students.githubUsername',
      })
  }

  return {
    ...guts,
    getStaleStudents,
  }
}