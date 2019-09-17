const query = `
  BEGIN;
    DELETE FROM "UsersCohorts";
    DELETE FROM "StudentsEvents";
    DELETE FROM "StudentsGithubResponses";
    DELETE FROM "Users";
    DELETE FROM "Students";
    DELETE FROM "Cohorts";
  END;
`

exports.seed = function(knex, Promise) {
  return knex.raw(query)
};
