function uuid () {
  return 'xxxx-xxxx-xxxx-xxxx-xxxx-xxxx-xxxx-xxxx'.replace(/x/g, function (c) {
    const r = (Math.random() * 16) | 0
    return r.toString(16)
  })
}

module.exports = uuid