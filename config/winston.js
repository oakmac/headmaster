const appRoot = require('app-root-path');
const winston = require('winston');
const { createLogger, format, transports } = require('winston');
const { combine, timestamp, label, printf } = format;

const transports = {
  console: new winston.transports.Console({ level: 'warn' }),
  file: new winston.transports.File({ filename: '${appRoot}/logs/app.log', level: 'info' })
};
const myFormat = printf(({ level, message, label, timestamp }) => {
  return `${timestamp} [${label}] ${level}: ${message}`;
});
const logger = winston.createLogger({
  format: combine(
    timestamp(),
    myFormat),
  transports: [
    transports.console,
    transports.file
  ]
});
