
exports.up = function(knex) {
  return knex.schema.table('Students', function (table) {
    table.dropUnique('githubUsername')
  })
};

exports.down = function(knex) {
  return knex.schema.alterTable('Students', function (table) {
    table.unique('githubUsername')
  })
};
