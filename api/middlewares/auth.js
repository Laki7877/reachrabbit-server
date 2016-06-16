/**
 * Handle server authentication
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var config  = require('config'),
    authService = require('../services/authService');

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
      req.auth = auth.decode(token, function(err, sub) {
        // some jwt decoding error
        if(err) {
          return next(new errors.AuthenticationRequiredError(err));
        }
        // pass auth forward
        //req.auth = sub.id;
        next();
      });
    }
  };
}
