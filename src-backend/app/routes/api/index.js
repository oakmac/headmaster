const router = require('express').Router()

const classesAPI = require('./classes')
const studentsAPI = require('./students')
const dashboardAPI = require('./dashboard')

router.use('/api', [
  classesAPI,
  studentsAPI,
  dashboardAPI,
])

module.exports = router