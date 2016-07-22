/**
 * Provide our app's auth methods
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';
var moment          = require('moment'),
    jwt             = require('jsonwebtoken'),
    crypto          = require('crypto'),
    config          = require('config'),
    Promise         = require('bluebird'),
    cacheHelper     = require('../helpers/cacheHelper');

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
      exp: moment().add(expirationTime, 'hours').unix(),
      hash: crypto.randomBytes(20).toString('hex') //make sure it is randomed
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
    }).catch(function() {
      return null;
    });
  },
  createToken: function(user, role, cache) {
    // cache user
    if(cache) {
      cacheHelper.set(user.userId, {
        user: user,
        role: role
      });
    }
    return this.encode({
      userId: user.userId
    });
  }
};