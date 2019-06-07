const TABLE_NAME = 'Classes'

function setupClassesSchema(knex, table) {

  table
    .string('id')
    .primary()

  table
    .string('slug')
    .unique()

  table
    .string('name')

  table
    .timestamp('createdAt')
    .defaultTo(knex.fn.now())

}

exports.up = function(knex) {
  return knex.schema
    .createTable(TABLE_NAME, setupClassesSchema.bind(null, knex))
}

exports.down = function(knex) {
  return knex.schema
    .dropTable(TABLE_NAME)
}
