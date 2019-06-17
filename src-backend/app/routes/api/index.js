const router = require('express').Router()

const classesAPI = require('./classes')

router.use('/api', [
  classesAPI,
])

module.exports = router