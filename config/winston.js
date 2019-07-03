const appRoot = require('app-root-path');
const winston = require('winston');
const { createLogger, format, transports } = require('winston');
const { combine, timestamp, label, printf } = format;

const logTransports = {
  console: new winston.transports.Console({ level: 'warn', maxsize: 1000 }),
  file: new winston.transports.File({ filename: `${appRoot}/logs/app.log`, level: 'info', maxsize: 1000 })
};

const myFormat = printf(({ level, message, label, timestamp }) => {
  return `${timestamp} [${label}] ${level}: ${message}`;
});

const logger = winston.createLogger({
  format: combine(
    timestamp(),
    myFormat,
    format.errors({ stack: true }),
    format.splat(),
    format.colorize(),
    format.json()),
  transports: [
    logTransports.console,
    logTransports.file
  ]
});
