/**
 * Handle user endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var brandService = require('../services/brandService'),
    influencerService = require('../services/influencerService'),
    userService = require('../services/userService'),
    mailService = require('../services/mailService'),
    authService = require('../services/authService'),
    facebookService = require('../services/facebookService'),
    cacheHelper = require('../helpers/cacheHelper'),
    sequelize = require('../models').sequelize,
    config = require('config');

module.exports = {
  /**
   * Create new influencer
   *
   * @param      {Object}    req     The request
   * @param      {Object}    res     The resource
   * @param      {Function}  next    The next
   */
  signupInfluencer: function(req, res, next) {
    //TODO: implement this by 7/6/2016
    var form = _.omit(req.body, ['profilePicture']);
    form.profilePicture = req.body.profilePicture.resourceId;

    sequelize.transaction(function(t) {
      return influencerService.create(form, t)
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
  },
  /**
   * Create new brand and automatically login to it (return token)
   *
   * @param      {Object}    req     The request
   * @param      {Object}    res     The resource
   * @param      {Function}  next    The next
   */
  signupBrand: function(req, res, next) {
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
  },
  /**
   * Find current user's information
   *
   * @param      {Object}    req     The request
   * @param      {Object}    res     The resource
   * @param      {Function}  next    The next
   */
  profile: function(req, res, next) {
    userService.findById(req.user.userId)
      .then(function(user) {
        if(!user) {
          // no user found
          return new errors.HttpStatusError(httpStatus.NOT_FOUND, config.ERROR.USER_NOT_FOUND);
        }
        return res.send(user);
      })
  }
};
