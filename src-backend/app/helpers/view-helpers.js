const fs = require('fs-plus')

function slurpFile (filename) {
  return fs.readFileSync(filename, {encoding: 'utf8'})
}

module.exports = {
  slurpFile,
}