const TABLE_NAME = 'UsersClasses'

function setupUsersClassesSchema(knex, table) {

  table
    .string('id')
    .primary()

  table
    .string('userId')
  table
    .foreign('userId')
    .references('Users.id')

  table
    .string('classId')
  table
    .foreign('classId')
    .references('Classes.id')

  table.enum('role', ['headmaster'])

  table
    .timestamp('createdAt')
    .defaultTo(knex.fn.now())

}

exports.up = function(knex) {
  return knex.schema
    .createTable(TABLE_NAME, setupUsersClassesSchema.bind(null, knex))
}

exports.down = function(knex) {
  return knex.schema
    .dropTable(TABLE_NAME)
}
