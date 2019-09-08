const R = require('ramda')

const OLD_TO_NEW_TABLENAMES = {
  Classes: 'Cohorts',
  UsersClasses: 'UsersCohorts',
}

const NEW_TO_OLD_TABLENAMES = R.invertObj(OLD_TO_NEW_TABLENAMES)

function renameTables(knex, tables) {
  const renamingPromises = R.pipe(
    R.mapObjIndexed((newTablename, oldTablename) => (
      knex.schema.renameTable(oldTablename, newTablename)
    )),
    R.values,
  )(tables)

  return Promise.all(renamingPromises)
}

exports.up = function(knex, Promise) {
  return renameTables(knex, OLD_TO_NEW_TABLENAMES)
    .then(() => {
      return knex.schema.alterTable('UsersCohorts', (t) => {
        t.renameColumn('classId', 'cohortId')
      })
    })
    .then(() => {
      return knex.schema.alterTable('Students', (t) => {
        t.renameColumn('classId', 'cohortId')
      })
    })
};

exports.down = function(knex, Promise) {
  return renameTables(knex, NEW_TO_OLD_TABLENAMES)
    .then(() => {
      return knex.schema.alterTable('UsersClasses', (t) => {
        t.renameColumn('cohortId', 'classId')
      })
    })
    .then(() => {
      return knex.schema.alterTable('Students', (t) => {
        t.renameColumn('cohortId', 'classId')
      })
    })
};
