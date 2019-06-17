const R = require('ramda')

function getConfig() {
  let config = {
    environmentVariables: {},
  }
  
  try {
    config = require('../config.json')
  } catch(noConfig) {
    // log message about setting up config
  }

  return config
}

function loadEnvironmentVariable(value, name) {
  process.env[name] = value
}

function loadEnvironmentVariables() {
  const { environmentVariables } = getConfig()

  R.forEachObjIndexed(
    loadEnvironmentVariable
  )(environmentVariables)

}


module.exports = {
  getConfig,
  loadEnvironmentVariables,
}