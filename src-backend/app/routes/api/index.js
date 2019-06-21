const router = require('express').Router()

const classesAPI = require('./classes')
const studentsAPI = require('./students')

router.use('/api', [
  classesAPI,
  studentsAPI,
])

module.exports = router