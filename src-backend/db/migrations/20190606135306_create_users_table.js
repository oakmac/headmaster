const TABLE_NAME = 'Users'

function setupUserSchema(knex, table) {

  table
    .string('id')
    .primary()

  table
    .string('email')

  table
    .string('githubId')

  table
    .string('githubUsername')

  table
    .string('displayName')

  table
    .timestamp('createdAt')
    .defaultTo(knex.fn.now())

}

exports.up = function(knex) {
  return knex.schema
    .createTable(TABLE_NAME, setupUserSchema.bind(null, knex))
}

exports.down = function(knex) {
  return knex.schema
    .dropTable(TABLE_NAME)
}
