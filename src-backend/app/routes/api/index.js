const router = require('express').Router()

const cohortsAPI = require('./cohorts')
const studentsAPI = require('./students')
const dashboardAPI = require('./dashboard')

router.use('/api', [
  cohortsAPI,
  studentsAPI,
  dashboardAPI,
])

module.exports = router