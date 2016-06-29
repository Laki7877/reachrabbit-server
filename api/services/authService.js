/**
 * Provide authentication method
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';
var moment          = require('moment'),
    jwt             = require('jsonwebtoken'),
    userCrud        = require('./crudService')('User'),
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
   * @return     {Promise} (token)
   */
  login: function(email, password) {
    var self = this;
    return userCrud.findOne({ email: email })
      .then(function(user) {
        // no user found
        if(!user) {
          throw new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid email or password');
        }
        return user;
      })
      .then(function(user) {
        return user.verifyPassword(password)
          .then(function(eq) {
            //not equal
            if(!eq) {
              throw new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid email/password');
            }
            return user.id;
          })
      })
      .then(function(id) {
        return self.encode(id);
      });
  },
  /**
   * Login with oauth
   *
   * @param      {String}    accessToken      Facebook access_token
   * @param      {Function}  done    The done
   */
  loginWithFacebook: function(accessToken) {
    var self = this;
    //TODO: implement this as generic function
  },

  _loginWithOauth: function(type, accessToken) {
    var self = this;
    var opts = {};
    //TODO: implement this
  }
};
