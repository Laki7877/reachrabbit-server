/**
 * Handle all passport strategies
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var passport = require('passport'),
    JwtStrategy = require('passport-jwt').Strategy,
    JwtExtractor = require('passport-jwt').ExtractJwt,
	User = require('./model').User;

module.exports = function(app, config) {
  // handle swagger config
  config.swaggerSecurityHandler = {
    jwt: function(req, def, scopes, done) {
      passport.authenticate('jwt', { session: false } , function(req, res) {

      });
    }
  };

	// init passport
	app.use(passport.initialize());
	app.use(passport.session());

    /**
     * Json web token strategy
     *
     * @description Handle authentication with json web token strategy. User is required to attach "Bearer <token>" to Authorization Header in each authorized endpoint request.
     * @see        https://github.com/themikenicholson/passport-jwt
     */
    passport.use(new JwtStrategy({
        secretOrKey: process.env.JWT_SECRET || 'mySecretKey',
        jwtFromRequest: JwtExtractor.fromAuthHeaderWithScheme('Bearer'),
    }, function(payload, done) {
        User.findOne({id: payload.sub}, function(user) {
            done(null, user);
        }, done);
    }));
}
