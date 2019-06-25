
exports.up = function(knex, Promise) {
  return knex.schema.alterTable('StudentsGithubResponses', function(t) {
    t.text('body', 'longtext').alter();
  })
};

exports.down = function(knex, Promise) {
  return knex.schema.alterTable('StudentsGithubResponses', function(t) {
    t.string('body').alter();
  })
};
