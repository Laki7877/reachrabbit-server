/**
 * Handle user endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var brandService = require('../services/brandService'),
    influencerService = require('../services/influencerService'),
    mailService = require('../services/mailService'),
    authService = require('../services/authService'),
    facebookService = require('../services/facebookService'),
    cacheHelper = require('../helpers/cacheHelper'),
    sequelize = require('../models').sequelize,
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
  var form = _.omit(req.body, ['profilePicture']);
  form.profilePicture = req.body.profilePicture.resourceId;

  sequelize.transaction(function(t) {
    return influencerService.create(form)
      .then(function(user) {
        return authService.createTokenForInfluencer(user, true);
      })
      .then(function(token) {
        return { token: token };
      });
  })
  .then(function(result) {
    return res.send(result);
  })
  .catch(next);
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

  // create transaction
  sequelize.transaction(function(t) {
    // create new brand
    return brandService.create(form, t)
      .then(function(user) {
        return authService.createTokenForBrand(user, true);
      })
      .then(function(token) {
        return { token: token };
      });
  })
  .then(function(result) {
    return res.send(result);
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
    return res.send(req.user);
  } else {
    return next(new errors.NotFoundError('User not found'));
  }
}

module.exports = {
  signupInfluencer: signupInfluencer,
  signupBrand: signupBrand,
  profile: profile
};
