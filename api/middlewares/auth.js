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

    if(_.isNil(authHeader)) {
      return next(new errors.AuthenticationRequiredError('No authorization header'));
    }

    // auth exist
    var splits = authHeader.split(' ');

    // invalid authentication
    if(splits.length !== 2 || splits[0] !== config.AUTHORIZATION_TYPE) {
      return next(new errors.AuthenticationRequiredError('Token'));
    }

    // get auth token
    var token = splits[1];

    return authService.decode(token)
      .then(function(id) {
        return authService.read(id);
      }, function(err) {
        throw new errors.AuthenticationRequiredError('Token');
      })
      .then(function(user) {
        if(!_.isNil(roles)) {
          // single arg
          if(_.isString(roles) && user.role !== roles) {
            //not role
            throw new errors.NotPermittedError('operation is forbidden');
          }
          // array args
          if(_.isArray(roles) && !_.includes(roles, user.role)){
            // not role
            throw new errors.NotPermittedError('operation is forbidden');
          }

          throw new errors.NotImplementedError('invalid auth middleware args type');
        }

        // assign to req
        req.user = user;
        return next();
      })
      .catch(next);
  };
};
