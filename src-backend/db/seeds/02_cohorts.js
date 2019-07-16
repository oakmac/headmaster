// FIXME: rename table "Classes" to "Cohorts"

exports.seed = function(knex, Promise) {
  // Deletes ALL existing entries
  return knex('Classes').del()
    .then(function () {
      // Inserts seed entries
      return knex('Classes').insert([
        {id: 'Class-8948c7b6-18da-4351-b457-16887d9d372e', slug: 'demo-class-1', name: 'Demo Class 1'},
        {id: 'Class-5d409547-df86-4020-84a7-8ac5a7925fb2', slug: 'demo-class-2', name: 'Demo Class 2'}
      ]);
    });
};
