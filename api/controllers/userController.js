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
          return influencerService.createToken(user, true);
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
    // create transaction
    sequelize.transaction(function(t) {
      // create new brand
      return brandService.create(form, t)
        .then(function(user) {
          return brandService.createToken(user, true);
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
    if(req.user) {
      // assign to current user's userid
      var user = req.body;
      user.userId = req.user.userId;

      // for brand
      if(req.role === config.ROLE.BRAND) {
        return sequelize.transaction(function(t) {
          brandService.update(user, t);
        })
        .catch(next);
      }

      // for influencer
      else if(req.role === config.ROLE.INFLUENCER) {

      }

      next(new errors.HttpStatusError(httpStatus.NOT_IMPLEMENTED));
    }
  }
};
