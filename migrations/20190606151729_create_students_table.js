const TABLE_NAME = 'Students'

function setupStudentsSchema(knex, table) {

  table
    .string('id')
    .primary()

  table
    .string('classId')
  table
    .foreign('classId')
    .references('Classes.id')

  table
    .string('displayName')

  table
    .string('githubUsername')
    .unique()

  table
    .timestamp('createdAt')
    .defaultTo(knex.fn.now())

}

exports.up = function(knex) {
  return knex.schema
    .createTable(TABLE_NAME, setupStudentsSchema.bind(null, knex))
}

exports.down = function(knex) {
  return knex.schema
    .dropTable(TABLE_NAME)
}
