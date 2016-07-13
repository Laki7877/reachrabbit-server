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
  createInfluencer: function(req, res, next) {
    var form = req.body;
    if(form.profilePicture) {
      form.profilePicture = form.profilePicture.resourceId;
    }

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
  createBrand: function(req, res, next) {
    var form = req.body;
    if(form.profilePicture) {
      form.profilePicture = form.profilePicture.resourceId;
    }

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
  getProfile: function(req, res, next) {
    if(req.user) {
      return res.send(req.user);
    } else {
      return next(new errors.HttpStatusError(httpStatus.UNAUTHORIZED, config.ERROR.NO_PERMISSION));
    }
  },
  updateProfile: function(req, res, next) {
    next();
  }
};
