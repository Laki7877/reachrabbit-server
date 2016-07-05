/**
 * Handle user endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var brandService = require('../services/brandService'),
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
  //TODO: implement this by 7/6/2016
}
/**
 * Create new brand and automatically login to it (return token)
 *
 * @param      {Object}    req     The request
 * @param      {Object}    res     The resource
 * @param      {Function}  next    The next
 */
function signupBrand(req, res, next) {
  // save profilePicture as resourceId
  var form = _.omit(req.body, ['profilePicture']);
  form.profilePicture = req.body.profilePicture.resourceId;

  // create new brand
  brandService.create(form)
    .then(function(user) {
      // cache user
      cacheHelper.set(user.userId, {
        user: user,
        role: config.ROLE.BRAND
      });
      return authService.encode({
        userId: user.userId
      });
    })
    .then(function(token) {
      return res.send({
        token: token
      });
    })
    .catch(next);
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
