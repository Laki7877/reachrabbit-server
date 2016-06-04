/**
 * Handle all passport strategies
 * 
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var passport = require('passport'),
	User = require('./api/model').User;

module.exports = function(app, config) {
	// init passport
	app.use(passport.initialize());
	app.use(passport.session());

	// serialization
	passport.serializeUser(User.serializeUser());
	passport.deserializeUser(User.deserializeUser());

	// use local strategy
	passport.use(User.createStrategy());
}