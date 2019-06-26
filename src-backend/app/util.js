const fs = require('fs-plus')
const path = require('path')

const viewsDir = path.resolve(__dirname, 'views')

function isArray (a) {
  return Array.isArray(a)
}

function isFn (f) {
  return typeof f === 'function'
}

function loadTemplate (templateName) {
  if (!templateName.endsWith('.mustache')) {
    templateName = templateName + '.mustache'
  }

  try {
    return slurpFile(path.join(viewsDir, templateName))
  } catch {
    console.error('Unable to load template file: ' + templateName)
    return null
  }
}

function slurpFile (filename) {
  return fs.readFileSync(filename, {encoding: 'utf8'})
}

module.exports = {
  isArray,
  isFn,
  loadTemplate,
  slurpFile,
}
