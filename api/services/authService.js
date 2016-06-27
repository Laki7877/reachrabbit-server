/**
 * Provide authentication method
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';
var moment          = require('moment'),
    jwt             = require('jsonwebtoken'),
    User            = require('../models').User,
    facebookService = require('./facebookService');

var secret          = process.env.JWT_SECRET;
var expirationTime  = process.env.JWT_EXPIRATION_TIME;

module.exports = {
  /**
   * Encode json web token
   *
   * @param      {Object}    subject  Encoding subject
   * @returns    {Promise} (encodedJWTString)
   */
  encode: function(subject) {
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
      exp: moment().add(expirationTime, 'hours').unix()
    };

    // turn callback to promise
    var sign = Promise.promisify(jwt.sign, { context: jwt });
    return sign(payload, secret, options);
  },
  /**
   * Decode json web token
   *
   * @param      {String}    token   Json web token
   * @return     {Promise} (decodedSubject)
   */
  decode: function(token) {
    var options = {};

    // turn callback to promise
    var verify = Promise.promisify(jwt.verify, { context: jwt });

    return verify(token, secret, options).then(function(decoded) {
      return decoded.sub;
    });
  },
  /**
   * Login with email and password
   *
   * @param      {String}  email     The email
   * @param      {String}  password  The password
   * @return     {Promise} (jwtToken)
   */
  login: function(email, password) {
    var self = this;
    return async.waterfall([
      // find user by email
      function() {
        return new Promise(function(resolve, reject) {
          User.findOne({
            where: {
              email: email
            }
          }).then(function(user) {
            // no user found
            if(_.isNil(user)) {
              return reject(new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid email/password'));
            }
            // not confirm yet
            if(!user.confirm) {
               return reject(new errors.HttpStatusError(httpStatus.NOT_ACCEPTABLE, 'account is unconfirmed'));
            }
            return resolve(user);
          }, reject);
        });
      },
      // verify password
      function(user) {
        return new Promise(function(resolve, reject) {
          user.verifyPassword(password)
            .then(function(eq) {
              if(!eq) {
                return reject(new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid email/password'));
              }
              return resolve(user.id);
            }, reject);
        });
      },
      // encode jwt
      function(id) {
        return self.encode(id);
      }
    ]);
  },
  /**
   * Login with facebook access token
   *
   * @param      {String}    accessToken      Facebook access_token
   * @param      {Function}  done    The done
   */
  loginWithFacebook: function(accessToken) {
    var self = this;
    async.waterfall([
      function() {
        return facebookService.getProfile(accessToken)
          .then(function(profile) {
            return profile.id;
          });
      },
      // fbid to userid
      function(id) {
        return new Promise(function(resolve, reject) {
          User.findOne({
            where: {
              facebookId: id
            }
          })
          .then(function(user) {
            // no user found
            if(_.isNil(user)) {
              return reject(new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid email/password'));
            }
            // not confirm yet
            /*if(!user.confirm) {
                return cb(new errors.HttpStatusError(httpStatus.NOT_ACCEPTABLE, 'user is not confirmed yet'));
            }*/
            return resolve(user.id);
          });
        });
      },
      // userid to jwt
      function(id) {
        return self.encode(id);
      }
    ]);
  }
};
