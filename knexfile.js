// Update with your config settings.
const { loadEnvironmentVariables } = require('./src-backend/utils/handle-config')

loadEnvironmentVariables()

module.exports = {

  development: {
    client: 'postgresql',
    connection: process.env.PG_CONNECTION_STRING,
    migrations: {
      tableName: 'knex_migrations',
      directory: `${__dirname}/src-backend/db/migrations`
    },
    seeds: {
      directory: `${__dirname}/src-backend/db/seeds`
    }
  },

  staging: {
    client: 'postgresql',
    connection: process.env.PG_CONNECTION_STRING,
    pool: {
      min: 2,
      max: 10
    },
    migrations: {
      tableName: 'knex_migrations',
      directory: `${__dirname}/src-backend/db/migrations`
    },
    seeds: {
      directory: `${__dirname}/src-backend/db/seeds`
    }
  },

  production: {
    client: 'postgresql',
    connection: process.env.PG_CONNECTION_STRING,
    pool: {
      min: 2,
      max: 10
    },
    migrations: {
      tableName: 'knex_migrations',
      directory: `${__dirname}/src-backend/db/migrations`
    },
    seeds: {
      directory: `${__dirname}/src-backend/db/seeds`
    }
  }

}
