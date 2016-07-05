/**
 * Handle user endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var userService = require('../services/userService'),
    crudService = require('../services/crudService')('User'),
    mailService = require('../services/mailService'),
    authService = require('../services/authService'),
    facebookService = require('../services/facebookService'),
    config = require('config');

/**
 * Create new influencer
 *
 * @param      {Object}    req     The request
 * @param      {Object}    res     The resource
 * @param      {Function}  next    The next
 */
function signupInfluencer(req, res, next) {
  if(!req.body.token) {
    return next(new errors.NotFoundError('Facebook Access Token'));
  }
  // create new user
  async.waterfall([
    // get facebook id by token
    facebookService.getId(req.body.token),
    function(id) {
      return crudService.findOne({

      });
    },
    // generate hash for email
    function(user) {
      return authService.encode(user.id)
        .then(function(hash) {
          return [user, hash];
        });
    }
  ]).then(function(result) {
    return res.json(result);
  }).catch(next);
}
/**
 * Create new brand
 *
 * @param      {Object}    req     The request
 * @param      {Object}    res     The resource
 * @param      {Function}  next    The next
 */
function signupBrand(req, res, next) {
  var form = _.pick(req.body);
  // create new user
  async.waterfall([
    userService.create(req.body)
  ]).then(function(result) {
    return res.json(result);
  }).catch(next);
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
  signupInfluencer: signupInfluencer,
  signupBrand: signupBrand,
  profile: profile
};
