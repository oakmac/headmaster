
const demoUser1Id = 'User-239b591c-41a0-4f26-ad3c-4afaa7edd662'
const demoUser2Id = 'User-5f203608-20d7-46c8-a3fa-4c0d2c79bcdd'

exports.seed = function(knex, Promise) {
  // Deletes ALL existing entries
  return knex('StudentsEvents').del()
    .then(function () {
      // Inserts seed entries
      return knex('StudentsEvents').insert([
        // Rich Hickey
        {id: 'Touchpoint-1cd8f592-0299-493f-ba30-95236d4beef0', studentId: 'Student-07be7b0c-80c9-4480-a540-57db661806b8', userId: demoUser1Id, body: JSON.stringify({comment: "Rich really nailed the DOM exercise. He was very excited about it.", stoplight: "green", tags: [{label: "High Comprehension", status: "good"}, {label: "High Motivation", status: "good"}]})},

        // Jeremy Ashkenas
        {id: 'Touchpoint-12f3cfcf-bf03-43ed-83c8-b5894467c57e', studentId: 'Student-2a3c740e-603e-4d20-9061-c45b26611239', userId: demoUser1Id, body: JSON.stringify({comment: "", stoplight: "green"})},

        // David Nolen
        {id: 'Touchpoint-e25325e4-00f5-4ec5-bb22-534ce739681f', studentId: 'Student-91d0ba84-a18b-4faa-877f-234673907f24', userId: demoUser1Id, body: JSON.stringify({comment: "", stoplight: "yellow"})},

        // Guido van Rossum
        {id: 'Touchpoint-94fa167e-db36-47fc-8c19-be16617e39e7', studentId: 'Student-598862e7-6739-47bd-a6aa-dc9a42cd60b0', userId: demoUser1Id, body: JSON.stringify({comment: "", stoplight: "red"})},

        // Rebecca Murphey
        {id: 'Touchpoint-a0cbf60e-6a90-4fd9-ae37-e222d907ac61', studentId: 'Student-ffa52734-0d5d-4b70-bf4d-fb5e7bd1f232', userId: demoUser2Id, body: JSON.stringify({comment: "", stoplight: "green"})},

        // Mike Fikes
        {id: 'Touchpoint-61ca1649-41c8-4052-8760-350872fab832', studentId: 'Student-61520e06-581d-4bf7-b748-06b1728a1818', userId: demoUser2Id, body: JSON.stringify({comment: "", stoplight: "yellow"})},

        // John Resig
        {id: 'Touchpoint-cf5c2138-4881-467b-9fe8-28fed6f53179', studentId: 'Student-063f3ed4-808c-4296-b152-602e5b72ae29', userId: demoUser1Id, body: JSON.stringify({comment: "", stoplight: "red"})},

        // Mike Bostock
        {id: 'Touchpoint-d0aa8f8f-5fac-4877-bb33-f7d61f85a6d9', studentId: 'Student-0dad8e75-7f35-4b10-927f-a3a94f721363', userId: demoUser1Id, body: JSON.stringify({comment: "", stoplight: "yellow"})},

        // Linus Torvalds
        {id: 'Touchpoint-ff8a73a9-705d-4e04-ba1b-0670d0248bf4', studentId: 'Student-cbc8a496-3fa9-4ef8-9b0c-262391184c7c', userId: demoUser1Id, body: JSON.stringify({comment: "", stoplight: "yellow"})},

        // José Valim
        {id: 'Touchpoint-c2251af8-3720-462c-b1af-806d31d5319f', studentId: 'Student-5771b1d3-bc19-49f3-8791-2dde5601dc7b', userId: demoUser1Id, body: JSON.stringify({comment: "", stoplight: "green"})}
      ]);
    });
};
