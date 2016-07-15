/**
 * Handle server authentication
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var config  = require('config'),
    userService = require('../services/userService'),
    authHelper = require('../helpers/authHelper'),
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
    return authHelper.decode(token)
      .then(function(decoded) {
        // get cached object from cache
        if(!decoded) {
          throw new errors.HttpStatusError(httpStatus.UNAUTHORIZED, config.ERROR.NO_PERMISSION);
        }
        return cacheHelper.get(decoded.userId)
          .then(function(data) {
            if(!data) {
              return [false, decoded];
            }
            return [true, data];
          });
      })
      .spread(function(found, data) {
        if(!found) {
          return userService.findById(data.userId)
            .then(function(user) {
              // no user found
              var role = null;
              if(!user) {
                throw new errors.HttpStatusError(httpStatus.UNAUTHORIZED, config.ERROR.NO_PERMISSION);
              }

              console.log(user);

              // assign arbitary role
              if(user.Brand) {
                role = config.ROLE.BRAND;
              }
              else if(user.Influencer) {
                role = config.ROLE.INFLUENCER;
              }
              return {user: user, role: role};
            });
        }
        return data;
      })
      .then(function(data) {
        // no roles
        if(!_.isNil(roles)) {
          // single role arg
          if(_.isString(roles) && data.role !== roles){
            //not role
            throw new errors.HttpStatusError(httpStatus.UNAUTHORIZED, config.ERROR.NO_PERMISSION);
          }
          // array of roles (OR condition)
          if(_.isArray(roles) && !_.includes(roles, data.role)){
            // not role
            throw new errors.HttpStatusError(httpStatus.UNAUTHORIZED, config.ERROR.NO_PERMISSION);
          }
        }
        // cannot find user
        if(!data.user) {
          throw new errors.HttpStatusError(httpStatus.UNAUTHORIZED, config.ERROR.NO_TOKEN);
        }
        // put onto req for next usage
        req.user = data.user;
        req.role = data.role;
        return next();
      })
      .catch(next);
  };
};
