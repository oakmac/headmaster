const fs = require('fs-plus')

function slurpFile (filename) {
  return fs.readFileSync(filename, {encoding: 'utf8'})
}

function isFn (f) {
  return typeof f === 'function'
}

module.exports = {
  isFn,
  slurpFile,
}
