
function convertToJSONB(knex, tablename) {
  return knex.schema.alterTable(tablename, function(t) {
    t.jsonb('body').alter();
  })
}

function convertToLongText(knex, tablename) {
  return knex.schema.alterTable(tablename, function(t) {
    t.text('body', 'longtext').alter();
  })
}

exports.up = function(knex, Promise) {
  return convertToJSONB(knex, 'StudentsEvents')
    .then(convertToJSONB.bind(null, knex, 'StudentsGithubResponses'))
};

exports.down = function(knex, Promise) {
  return convertToLongText(knex, 'StudentsEvents')
    .then(convertToLongText.bind(null, knex, 'StudentsGithubResponses'))
};
