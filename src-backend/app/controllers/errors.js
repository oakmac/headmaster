// TODO: send real page here; not just <h1>
function notFoundPage (req, res, _nextFn) {
  res.status(404)
     .send('<h1>page not found</h1>')
}

module.exports = {
  notFoundPage,
}
