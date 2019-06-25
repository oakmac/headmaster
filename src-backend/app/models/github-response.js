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
    return knex.from(tableName)
      .groupBy('studentId')
      .orderBy('createdAt', 'desc')
      .whereNotNull('body')
      .select(knex.raw(`max(${tableName}.createdAt) as createdAt, studentId, body`))
  } 

  // TODO make more flexible in time units since
  function getStaleStudents(classId, daysSince = 1) {
    return  knex.from('Students')
      .leftJoin(
        getLatestResponse().as('latest'),
        'Students.id', `latest.studentId`
      )
      .where('Students.classId', classId)
      .andWhere(function(){
        this
          .where('latest.createdAt', '<', subDays(new Date(), daysSince))
          .orWhereNull('latest.createdAt')
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