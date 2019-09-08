'use strict'

const initCohortModel = require('./cohort')
const R = require('ramda')

const createGuts = require('../helpers/model-guts')
const {
  compareIncomingAndExistingItems,
} = require('../helpers/model-helpers')

const name = 'UserCohort'
const tableName = 'UsersCohorts'

const selectableProps = [
  'id',
  'userId',
  'cohortId',
  'role',
  'createdAt',
]

module.exports = knex => {
  const guts = createGuts({
    knex,
    name,
    tableName,
    selectableProps
  })

  const Cohort = initCohortModel(knex)

  const joinedCohortsSubquery = knex
    .from(tableName)
    .leftJoin(
      'Cohorts',
      `${tableName}.cohortId`, 'Cohorts.id',
    )

  function selectCohortsByCohortIds(cohorts) {
    return knex
      .from(tableName)
      .whereIn(
        'cohortId',
        R.map(
          R.prop('id'),
        )(cohorts)
      )
  }

  function setCohortsForUser(userId, cohorts) {
    return knex.transaction(function(transaction) {
      // get existing cohorts of user to compare
      return joinedCohortsSubquery.clone()
        .where({
          userId,
        })
        .transacting(transaction)
        .select()
        .then(function( existingUserCohorts ) {
          const existingCohorts = R.map(
            R.pipe(
              R.prop('cohortId'),
              R.objOf('id')
            )
          )(existingUserCohorts)
          const {
            itemsToRelate: cohortsToRelate,
            itemsToUnlink: cohortsToUnlink,
            itemsToInsert: cohortsToInsert,
          } = compareIncomingAndExistingItems(cohorts, existingCohorts)

          return selectCohortsByCohortIds(cohortsToUnlink)
            .del()
            .transacting(transaction)
            .then(function() {

              const cohortsToRelateToUser = R.difference(
                R.map(R.pick(['id']))(cohortsToRelate),
                existingCohorts
              )

              return Promise.all(R.map(function (cohortToUpdate) {
                return guts
                  ._create({
                    cohortId: cohortToUpdate.id,
                    userId,
                    role: 'headmaster',
                  })
                  .transacting(transaction)
              })(cohortsToRelateToUser))
            })
            .then(function() {
              if (R.isEmpty(cohortsToInsert)) {
                return cohortsToInsert
              }
              return Cohort.bulkCreate(cohortsToInsert)
                .transacting(transaction)
                .then(function() {
                  const slugs = R.map(R.prop('slug'))(cohortsToInsert)
                  return Cohort.find((builder) => (
                      builder.whereIn('slug', slugs)
                    )).transacting(transaction)
                })
                .then(function (cohorts) {
                  const arrayOfRelations = R.map((cohortToRelate) => ({
                    cohortId: cohortToRelate.id,
                    userId,
                    role: 'headmaster',
                  }))(cohorts)
                  return guts.bulkCreate(arrayOfRelations)
                    .transacting(transaction)
                })
            })
        })
    })
  }

  function getCohortsForUser(userId) {
    return joinedCohortsSubquery.clone()
      .where({
        userId,
      })
      .orderBy('Cohorts.createdAt', 'desc')
      .select()
  }

  return {
    ...guts,
    getCohortsForUser,
    setCohortsForUser,
  }
}