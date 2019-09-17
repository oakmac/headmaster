exports.seed = function (knex, Promise) {
  // Deletes ALL existing entries
  return knex('Users').del()
    .then(function () {
      // Inserts seed entries
      return knex('Users').insert([
        { id: 'User-239b591c-41a0-4f26-ad3c-4afaa7edd662', email: 'demouser1@example.com', githubId: '12345', githubUsername: 'demo_user_1', displayName: 'Demo User 1' },
        { id: 'User-5f203608-20d7-46c8-a3fa-4c0d2c79bcdd', email: 'demouser2@example.com', githubId: '82737', githubUsername: 'demo_user_2', displayName: 'Demo User 2' },
        { id: 'User-aced201f-5d85-466e-acbd-2cb83a37c58e', email: 'demouser3@example.com', githubId: '15232', githubUsername: 'demo_user_3', displayName: 'Demo User 3' }
      ])
    })
}
