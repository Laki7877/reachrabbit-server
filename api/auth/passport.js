/**
 * Handle all passport strategies
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var passport = require('passport'),
    redis = require('../../redis.js'),
    JwtStrategy = require('passport-jwt').Strategy,
    JwtExtractor = require('passport-jwt').ExtractJwt,
	  User = require('../models').User;

module.exports = function(app, config) {
  // handle swagger security
  config.swaggerSecurityHandlers = {
    JWT: function(req, def, scopes, done) {
      passport.authenticate('jwt', { session: false } , function(err, user, info) {
        if(err) return done(err);
        if(_.isNil(user)) {
          return done(new errors.httpStatusError(httpStatus.UNAUTHORIZED, 'Token is invalid'));
        }
        req.user = user;
        return done();
      })(req, null, done);
    }
  };

	// init passport
	app.use(passport.initialize());
	app.use(passport.session());

  /**
   * Json Web Token strategy
   *
   * @description Handle authentication with json web token strategy. User is required to attach "Bearer <JWT token>" to Authorization Header in each authorized endpoint request.
   * @see        https://github.com/themikenicholson/passport-jwt
   */
  passport.use(new JwtStrategy({
      secretOrKey: process.env.JWT_SECRET || 'mySecretKey',
      jwtFromRequest: JwtExtractor.fromAuthHeaderWithScheme('Bearer'),
  }, function(payload, done) {
      // check key in cache
      redis.hexists(payload.id, function(err, exist) {
        if(err) return done(err);
        if(exist) {
          // return from cache
          redis.hget(payload.id, function(err, result) {
            if(err) return done(err);
            return done(null, CJSON.parse(result));
          });
        }
        else {
          // fetch from db
          User.findOne({
            where: {id: payload.id}
          })
          .then(function(user) {
            // save to cache
            redis.hset(payload.id, CJSON.stringify(user));
            return done(null, user);
          })
          .catch(done);
        }
      });
  }));
};
