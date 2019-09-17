exports.seed = function (knex, Promise) {
  // Deletes ALL existing entries
  return knex('Students').del()
    .then(function () {
      // Inserts seed entries
      return knex('Students').insert([
        { id: 'Student-07be7b0c-80c9-4480-a540-57db661806b8', cohortId: 'Cohort-8948c7b6-18da-4351-b457-16887d9d372e', displayName: 'Rich Hickey', githubUsername: 'richhickey' },
        { id: 'Student-2a3c740e-603e-4d20-9061-c45b26611239', cohortId: 'Cohort-8948c7b6-18da-4351-b457-16887d9d372e', displayName: 'Jeremy Ashkenas', githubUsername: 'jashkenas' },
        { id: 'Student-91d0ba84-a18b-4faa-877f-234673907f24', cohortId: 'Cohort-8948c7b6-18da-4351-b457-16887d9d372e', displayName: 'David Nolen', githubUsername: 'swannodette' },
        { id: 'Student-598862e7-6739-47bd-a6aa-dc9a42cd60b0', cohortId: 'Cohort-8948c7b6-18da-4351-b457-16887d9d372e', displayName: 'Guido van Rossum', githubUsername: 'gvanrossum' },
        { id: 'Student-ffa52734-0d5d-4b70-bf4d-fb5e7bd1f232', cohortId: 'Cohort-8948c7b6-18da-4351-b457-16887d9d372e', displayName: 'Rebecca Murphey', githubUsername: 'rmurphey' },
        { id: 'Student-61520e06-581d-4bf7-b748-06b1728a1818', cohortId: 'Cohort-8948c7b6-18da-4351-b457-16887d9d372e', displayName: 'Mike Fikes', githubUsername: 'mfikes' },
        { id: 'Student-063f3ed4-808c-4296-b152-602e5b72ae29', cohortId: 'Cohort-8948c7b6-18da-4351-b457-16887d9d372e', displayName: 'John Resig', githubUsername: 'jeresig' },
        { id: 'Student-0dad8e75-7f35-4b10-927f-a3a94f721363', cohortId: 'Cohort-8948c7b6-18da-4351-b457-16887d9d372e', displayName: 'Mike Bostock', githubUsername: 'mbostock' },
        { id: 'Student-cbc8a496-3fa9-4ef8-9b0c-262391184c7c', cohortId: 'Cohort-8948c7b6-18da-4351-b457-16887d9d372e', displayName: 'Linus Torvalds', githubUsername: 'torvalds' },
        { id: 'Student-5771b1d3-bc19-49f3-8791-2dde5601dc7b', cohortId: 'Cohort-8948c7b6-18da-4351-b457-16887d9d372e', displayName: 'José Valim', githubUsername: 'josevalim' }
      ])
    })
}
