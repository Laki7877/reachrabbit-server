/**
 * Handle server authentication
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var config  = require('config'),
    AuthService = require('../services/AuthService'),
    UserService = require('../services/UserService');

module.exports = function(roles) {
  /**
   * Auth middleware function
   *
   * @param      {Object}    req     The request
   * @param      {Object}    res     The resource
   * @param      {Function}  next    The next
   */
  return function(req, res, next) {
    var authHeader = req.get('Authorization');

    // auth exist
    if(!_.isNil(authHeader)) {
      var splits = authHeader.split(' ');

      // invalid authentication
      if(splits.length !== 2 || splits[0] !== config.AUTHORIZATION_TYPE) {
        return next(new errors.AuthenticationRequiredError('Invalid authorization header'));
      }

      // get auth token
      var token = splits[1];
      async.waterfall([
        function(cb) {
          // decode jwt token to userid
          AuthService.decode(token, cb);
        },
        function(id, cb) {
          // get user by id
          UserService.read(id, cb);
        }
      ], function(err, user) {
        if(err) {
          return next(new errors.AuthenticationRequiredError(err));
        }
        req.user = user; // pass user forward
        return next();
      });
    } else {
      // no auth header
      return next(new errors.AuthenticationRequiredError('Required authorization header'));
    }
  };
};
