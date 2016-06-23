/**
 * Handle user endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var UserService = require('../services/UserService');

/**
 * Create new influencer
 *
 * @param      {Object}    req     The request
 * @param      {Object}    res     The resource
 * @param      {Function}  next    The next
 */
function registerInfluencer(req, res, next) {
  // create new user
  UserService.create(req.body, function(err, result) {
    if(err) {
      return next(err);
    }
    return res.json(result);
  });
}

/**
 * Find current user's information
 *
 * @param      {Object}    req     The request
 * @param      {Object}    res     The resource
 * @param      {Function}  next    The next
 */
function profile(req, res, next) {
  if(req.user) {
    return res.json(req.user);
  } else {
    return next(new errors.NotFoundError('User not found'));
  }
}

module.exports = {
  registerInfluencer: registerInfluencer,
  //registerBrand: registerBrand,
  //login: login,
  //confirmEmail: confirmEmail,
  profile: profile
};
