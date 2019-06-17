const TABLE_NAME = 'StudentsEvents'

function setupStudentsEventsSchema(knex, table) {

  table
    .string('id')
    .primary()

  table
    .string('studentId')
  table
    .foreign('studentId')
    .references('Students.id')

  table
    .string('userId')
  table
    .foreign('userId')
    .references('Users.id')

  table
    .string('body')

  table
    .timestamp('createdAt')
    .defaultTo(knex.fn.now())

}

exports.up = function(knex) {
  return knex.schema
    .createTable(TABLE_NAME, setupStudentsEventsSchema.bind(null, knex))
}

exports.down = function(knex) {
  return knex.schema
    .dropTable(TABLE_NAME)
}
