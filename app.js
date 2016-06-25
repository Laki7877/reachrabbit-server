/**
 * Express app entry point
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @fixer      Pat Sabpisa <ssabpisa@me.com>
 * @since      0.0.1
 */
'use strict';

// set process.env from .env
// require('dotenv').config();


// set default config folder
process.env.NODE_CONFIG_DIR = './api/config';

// global variables
global._          = require('lodash'); // your best friend
global.async      = require('async'); // async lib
global.errors     = require('common-errors'); // handle error object
global.httpStatus = require('http-status'); // access http status by name
global.CJSON      = require('circular-json'); // handle circular json
global.winston    = require('winston'); // console logging module

// app modules
var app         = require('express')(),
    https       = require('https'),
    http        = require('http'),
    fs          = require('fs'),
    path        = require('path'),
    cors        = require('cors'),

    bodyParser  = require('body-parser'), // parse request body
    prettyError = require('pretty-error'), // make error log pretty
    morgan      = require('morgan'), // express logging module
    handler     = require('errorhandler'), //report error back to client (dev)
    router      = require('./api/router.js');

/********************************
 * Middleware
 ********************************/
app.use(cors());
app.use(bodyParser.urlencoded({ limit: '50mb', extended: false }));
app.use(bodyParser.json({limit: '50mb'})); // parse json
app.use(errors.middleware.crashProtector()); // prevent server failure on async crash

// development mode
if(process.env.NODE_ENV === 'development') {
  app.use(handler()); // throw server error to client
  app.use(morgan('dev')); // log express to stdout
}
// production mode
if(process.env.NODE_ENV === 'production') {
  var accessLog = fs.createWriteStream(path.join(__dirname, 'log/access.log'), {flags: 'a'});
  app.use(morgan('combined', { stream: accessLog })); // log express to file
}

app.use(router()); // api route
app.use(errors.middleware.errorHandler); // handle common-errors message

// make error pretty
prettyError.start();

/*******************************
 * Start Server
 *******************************/
var server = http.createServer(app);
var port = process.env.PORT || 3000;

server.listen(port, function() {
  console.log('Express API available at ' + port);
});

// for testing
module.exports = app;
