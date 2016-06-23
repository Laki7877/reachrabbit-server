/**
 * Provide authentication method
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';
var moment = require('moment'),
    jwt    = require('jsonwebtoken'),
    User = require('../models').User;

/**
 * Encode json web token
 *
 * @param      {Object}    subject  Encoding subject
 * @param      {Function}  done     The done
 */
function encode(subject, done) {
  var options = {};

  /**
   * JWT standard payload form
   *
   * @type       {Object}
   * @see        https://self-issued.info/docs/draft-ietf-oauth-json-web-token.html#rfc.section.4
   */
  var payload = {
    sub: subject,
    iat: moment().unix(),
    exp: moment().add(process.env.JWT_EXPIRATION_TIME || 24, 'hours').unix()
  };

  // encode jwt
  jwt.sign(payload, process.env.JWT_SECRET, options, done);
}

/**
 * Decode json web token
 *
 * @param      {String}    token   Json web token
 * @param      {Function}  done    The done
 */
function decode(token, done) {
  var options = {};
  // decode jwt
  jwt.verify(token, process.env.JWT_SECRET, options, function(err, decoded) {
    if(err) return done(err);
    done(null, decoded.sub);
  });
}

/**
 * Login with email and password
 *
 * @param      {String}  email     The email
 * @param      {String}  password  The password
 * @returns    {String} JWT Token
 */
function login(email, password, done) {
  async.waterfall([
    // find user by email
    function(cb) {
      User.findOne({
        where: {
          email: email
        }
      }).then(function(user) {
        // no user found
        if(_.isNil(user)) {
          return cb(new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid email/password'));
        }
        // not confirm yet
        if(!user.confirm) {
          return cb(new errors.HttpStatusError(httpStatus.NOT_ACCEPTABLE, 'user is not confirmed yet'));
        }
        return cb(null, user);
      }).catch(cb);
    },
    // verify password
    function(user, cb) {
      user.verifyPassword(password, function(err, eq) {
        if(err) return cb(err);

        // not match
        if(!eq) {
          return cb(new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid email/password'));
        }
        return cb(null, user.id);
      });
    },
    // encode jwt
    function(id, cb) {
      encode(id, cb);
    }
  ], done);
}

/**
 * Login with facebook id
 *
 * @param      {String}    id      The identifier
 * @param      {Function}  done    The done
 */
function loginWithFB(id, done) {
  async.waterfall([
    function(cb) {
      User.findOne({
        where: {
          facebookId: id
        }
      }).then(function(user) {
        // no user found
        if(_.isNil(user)) {
          return cb(new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid email/password'));
        }
        // not confirm yet
        /*if(!user.confirm) {
            return cb(new errors.HttpStatusError(httpStatus.NOT_ACCEPTABLE, 'user is not confirmed yet'));
        }*/
        return cb(null, user.id);
      }).catch(cb);
    },
    function(id, cb) {
      encode(id, cb);
    }
  ], done);
}

// export modules
module.exports = {
  encode: encode,
  decode: decode,
  login: login,
  loginWithFB: loginWithFB
};
