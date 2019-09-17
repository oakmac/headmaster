exports.seed = function (knex, Promise) {
  // Deletes ALL existing entries
  return knex('Cohorts').del()
    .then(function () {
      // Inserts seed entries
      return knex('Cohorts').insert([
        { id: 'Cohort-8948c7b6-18da-4351-b457-16887d9d372e', slug: 'demo-cohort-1', name: 'Demo Cohort 1' },
        { id: 'Cohort-5d409547-df86-4020-84a7-8ac5a7925fb2', slug: 'demo-cohort-2', name: 'Demo Cohort 2' }
      ])
    })
}
