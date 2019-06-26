'use strict'

const initClassModel = require('./class')
const R = require('ramda')

const createGuts = require('../helpers/model-guts')
const {
  compareIncomingAndExistingItems,
} = require('../helpers/model-helpers')

const name = 'UserClass'
const tableName = 'UsersClasses'

const selectableProps = [
  'id',
  'userId',
  'classId',
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

  const Class = initClassModel(knex)

  const joinedClassesSubquery = knex
    .from(tableName)
    .leftJoin(
      'Classes',
      `${tableName}.classId`, 'Classes.id',
    )

  function selectClassesByClassIds(classes) {
    return knex
      .from(tableName)
      .whereIn(
        'classId',
        R.map(
          R.prop('id'),
        )(classes)
      )
  }

  function setClassesForUser(userId, classes) {
    return knex.transaction(function(transaction) {
      // get existing classes of user to compare
      return joinedClassesSubquery.clone()
        .where({
          userId,
        })
        .transacting(transaction)
        .select()
        .then(function( existingUserClasses ) {
          const existingClasses = R.map(
            R.pipe(
              R.prop('classId'),
              R.objOf('id')
            )
          )(existingUserClasses)
          const {
            itemsToRelate: classesToRelate,
            itemsToUnlink: classesToUnlink,
            itemsToInsert: classesToInsert,
          } = compareIncomingAndExistingItems(classes, existingClasses)

          return selectClassesByClassIds(classesToUnlink)
            .del()
            .transacting(transaction)
            .then(function() {

              const classesToRelateToUser = R.difference(
                R.map(R.pick(['id']))(classesToRelate),
                existingClasses
              )

              return Promise.all(R.map(function (classToUpdate) {
                return guts
                  ._create({
                    classId: classToUpdate.id,
                    userId,
                    role: 'headmaster',
                  })
                  .transacting(transaction)
              })(classesToRelateToUser))
            })
            .then(function() {
              if (R.isEmpty(classesToInsert)) {
                return classesToInsert
              }
              return Class.bulkCreate(classesToInsert)
                .transacting(transaction)
                .then(function() {
                  const slugs = R.map(R.prop('slug'))(classesToInsert)
                  return Class.find((builder) => (
                      builder.whereIn('slug', slugs)
                    )).transacting(transaction)
                })
                .then(function (classes) {
                  const arrayOfRelations = R.map((classToRelate) => ({
                    classId: classToRelate.id,
                    userId,
                    role: 'headmaster',
                  }))(classes)
                  return guts.bulkCreate(arrayOfRelations)
                    .transacting(transaction)
                })
            })
        })
    })
  }

  function getClassesForUser(userId) {
    return joinedClassesSubquery.clone()
      .where({
        userId,
      })
      .orderBy('Classes.createdAt', 'desc')
      .select()
  }

  return {
    ...guts,
    getClassesForUser,
    setClassesForUser,
  }
}