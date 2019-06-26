const TABLE_NAME = 'StudentsGithubResponses'

function setupStudentsGithubResponsesSchema(knex, table) {

  table
    .string('id')
    .primary()

  table
    .string('studentId')
  table
    .foreign('studentId')
    .references('Students.id')

  table
    .text('body', 'longtext')

  table
    .timestamp('createdAt')
    .defaultTo(knex.fn.now())

}

exports.up = function(knex) {
  return knex.schema
    .createTable(TABLE_NAME, setupStudentsGithubResponsesSchema.bind(null, knex))
}

exports.down = function(knex) {
  return knex.schema
    .dropTable(TABLE_NAME)
}
