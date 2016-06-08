/**
 * Authentication methods
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var passport = require('passport'),
  jwt = require('jsonwebtoken'),
  moment = require('moment');
/**
 * Encode raw object to json web token
 *
 * @param      {object}    raw     raw object
 * @param      {Function}  done    The done for async
 * @return     {string}    If done is not specified, return sync
 */
function encode(raw, done) {
  return jwt.sign(raw, process.env.JWT_SECRET || 'mySecretKey', {}, done);
}
/**
 * Decode jwt token regardless of if token is valid
 *
 * @param      {string}  token   The token
 * @return     {object}  unsafe object
 */
function decode(token) {
  return jwt.decode(token);
}
/**
 * Verify if JWT is a valid token
 *
 * @param      {string}    token   The token
 * @param      {Function}  done    The done for async
 * @return     {boolean}    If done is not specified, return sync
 */
function verify(token, done) {
  return jwt.verify(token, process.env.JWT_SECRET || 'mySecretKey', {}, done);
}

/**
 * Encode JWT standard payload to token
 *
 * @param      {integer}    id      The identifier
 * @param      {Function}  done    The done
 */
function encodePayload(id, done) {
  var payload = {
    sub: id,
    iat: moment().unix(),
    exp: moment().add(process.env.JWT_EXPIRATION_TIME || 24, 'hours').unix()
  };

  return encode(payload, done);
}

module.exports = {
  encode: encode,
  decode: decode,
  verify: verify,
  encodePayload: encodePayload
};
