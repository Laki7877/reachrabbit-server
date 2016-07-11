/**
 * Handle server authentication
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var config  = require('config'),
    authService = require('../services/authService'),
    userService = require('../services/userService'),
    cacheHelper = require('../helpers/cacheHelper');

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
      return next(new errors.HttpStatusError(httpStatus.UNAUTHORIZED, config.ERROR.NO_TOKEN));
    }

    // auth exist
    var splits = authHeader.split(' ');

    // invalid authentication
    if(splits.length !== 2 || splits[0] !== config.AUTHORIZATION_TYPE) {
      return next(new errors.HttpStatusError(httpStatus.UNAUTHORIZED, config.ERROR.NO_TOKEN));
    }

    // get auth token
    var token = splits[1];
    return authService.decode(token)
      /*.then(function(decoded) {
        // get cached object from cache
        return cacheHelper.get(decoded.userId);
      })
      .catch(function(err) {
        // invalid token
        throw new errors.HttpStatusError(httpStatus.UNAUTHORIZED, config.ERROR.NO_TOKEN);
      })*/
      .then(function(data) {
        if(!_.isNil(roles)) {
          // single role arg
          if(_.isString(roles) && data.role !== roles) {
            //not role
            throw new errors.HttpStatusError(httpStatus.UNAUTHORIZED, config.ERROR.NO_PERMISSION);
          }
          // array of roles (OR condition)
          if(_.isArray(roles) && !_.includes(roles, data.role)){
            // not role
            throw new errors.HttpStatusError(httpStatus.UNAUTHORIZED, config.ERROR.NO_PERMISSION);
          }
          throw new errors.HttpStatusError(httpStatus.NOT_IMPLEMENTED);
        }
        if(!data) {
          throw new errors.HttpStatusError(httpStatus.UNAUTHORIZED, config.ERROR.NO_TOKEN);
        }
        // put onto req for next usage
        req.user = data;
        return next();
      })
      .catch(next);
  };
};
