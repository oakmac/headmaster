
exports.seed = function(knex, Promise) {
  // Deletes ALL existing entries
  return knex('UsersClasses').del()
    .then(function () {
      // Inserts seed entries
      return knex('UsersClasses').insert([
        {id: 'UserClass-8a672548-07ec-4757-b6a4-6269240a5c20', userId: 'User-239b591c-41a0-4f26-ad3c-4afaa7edd662', classId: 'Class-8948c7b6-18da-4351-b457-16887d9d372e', role: 'headmaster'},
        {id: 'UserClass-e4a60355-d139-46e8-b901-576bd9a0f8ea', userId: 'User-5f203608-20d7-46c8-a3fa-4c0d2c79bcdd', classId: 'Class-8948c7b6-18da-4351-b457-16887d9d372e', role: 'headmaster'}
      ]);
    });
};
