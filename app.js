/**
 * Express app entry point
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

// .env configuration
require('dotenv').config();

// global var
global._ = require('lodash'); // your best util friend
global.logger = require('tracer').colorConsole(); // elegant version of console
global.async = require('async'); // async library
global.errors = require('common-errors'); // express common-errors
global.httpStatus = require('http-status'); // http status code by name

require('./redis.js')(function(redis) {
  global.redis = redis;
});

var SwaggerExpress = require('swagger-express-mw'),
	app = require('express')(),

	// packages
	session = require('express-session'), //session handler
	handler = require('errorhandler'), // reporting error to client (dev-only)
	morgan = require('morgan'), // logging api access
	cors = require('cors'),

	// sys packages
	fs = require('fs'),
	path = require('path'),

	// config
	config = { appRoot: __dirname };
/**
 * Middleware
 */
app.use(cors());
app.use(errors.middleware.crashProtector());

// use passport
require('./api/auth/passport.js')(app, config);

// development-only
if (process.env.NODE_ENV === 'development') {
	app.use(handler());
	app.use(morgan('dev'));
}
// production-only
else if (process.env.NODE_ENV === 'production') {
	var accessLogStream = fs.createWriteStream(path.join(__dirname, 'log/access.log'), {flags: 'a'});
	app.use(morgan('combined', { stream: accessLogStream}));
}

/**
 * App initialize
 */
SwaggerExpress.create(config, function(err, swaggerExpress) {
  if (err) { throw err; }
  // install middleware
  swaggerExpress.register(app);
  app.use(errors.middleware.errorHandler);

  // app start
  app.listen(process.env.PORT || 3000);
});

module.exports = app; // for testing
