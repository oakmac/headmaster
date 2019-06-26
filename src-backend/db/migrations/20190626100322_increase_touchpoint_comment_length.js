
// NOTE: sqlite does not support ALTER TABLE
// it is fine to skip this migration for sqlite

exports.up = function(knex) {
  if (knex.client.config.client !== 'sqlite3') {
    return knex.schema.alterTable('StudentsEvents', function(t) {
      t.text('body', 'longtext').alter();
    })
  } else {
    // no-op, but migrations require returning a promise
    return knex.schema.hasTable('StudentsEvents')
  }
};

exports.down = function(knex) {
  // no-op, but migrations require returning a promise
  return knex.schema.hasTable('StudentsEvents')
};
