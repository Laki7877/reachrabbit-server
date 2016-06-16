/**
 * Provide user service
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */ 
'use strict';

var User = require('../models').User;

/**
 * Login with email and password
 *
 * @param      {String}  email     The email
 * @param      {String}  password  The password
 */
function login(email, password, done) {
  async.waterfall([
    function(cb) {
      // find user by email
      User.findOne({
        where: {
          email: email
        }
      }).then(function(user) {
        // no user found
        if(_.isNil(user)) {
          return cb(new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid username/password'));
        }
        return cb(null, user);
      }).catch(cb);
    },
    function(user, cb) {
      // verify password
      user.verifyPassword(password, function(err, eq) {
        if(err) return cb(err);

        // not match
        if(!eq) {
          return cb(new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid username/password'));
        }
        return cb(null, user);
      })
    }
  ], done);
}

/**
 * find user by id
 *
 * @param      {String}    id      UUID
 * @param      {Function}  done    The done
 */
function findById(id, done) {
  User.findById(id).then(function(user) {
    if(_.isNil(user)) {
      return done(null, user);
    }
  }).catch(done);
}

// module export
module.exports = {
  login: login,
  findByEmail: findByEmail
};
