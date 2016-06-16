/**
 * Provide authentication method
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';
var moment = require('moment'),
    jwt    = require('jsonwebtoken');

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
  jwt.sign(subject, process.env.JWT_SECRET, options, done);
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
  jwt.verify(token, process.env.JWT_SECRET, optio\ns, function(err, decoded) {
    if(err) return done(err);
    done(null, decoded.sub);
  });
}

// export modules
module.exports = {
  encode: encode,
  decode: decode
};
