/**
 * Handle server authentication
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var config  = require('config'),
    authService = require('../services/authService'),
    userService = require('../services/userService');

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
          authService.decode(token, cb);
        },
        function(id, cb) {
          // get user by id
          userService.read(id, cb);
        }
      ], function(err, user) {
        // internal error
        if(err) {
          return next(new errors.AuthenticationRequiredError(err));
        }
        // no user
        if(!user) {
          return next(new errors.AuthenticationRequiredError(err));
        }
        // check roles
        if(!_.isNil(roles)) {
          if(_.isString(roles) && user.role !== roles) {
            // wrong role
            return next(new errors.AuthenticationRequiredError(err));
          } else if(_.isArray(roles) && !_.includes(roles, user.role)) {
            // wrong role
            return next(new errors.AuthenticationRequiredError(err));
          } else {
            // no roles?
            return next(new errors.HttpStatusError(httpStatus.INTERNAL_SERVER_ERROR, 'role not found'));
          }
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
