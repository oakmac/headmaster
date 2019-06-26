const { isFn, loadTemplate } = require('../util')

// TODO: send real page here; not just <h1>
function notFoundPage (req, res, _nextFn) {
  res.status(404)
     .send('<h1>page not found</h1>')
}

const permissionRequiredPage = loadTemplate('errors.permission-required')

function permissionRequired (req, res, nextFn) {
  if (isFn(req.isAuthenticated) && req.isAuthenticated()) {
    nextFn()
  } else {
    res.status(401).send(permissionRequiredPage)
  }
}

module.exports = {
  notFoundPage,
  permissionRequired,
}
