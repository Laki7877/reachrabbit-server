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

    sequelize.transaction(function(t) {
      return influencerService.create(form, t);
    })
    .then(function(user) {
      return influencerService.createToken(user, true);
    })
    .then(function(result) {
      return res.send({ token: result });
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
      return brandService.create(form, t);
    })
    .then(function(user) {
      return brandService.createToken(user, true);
    })
    .then(function(result) {
      return res.send({ token: result });
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
  /**
   * Update your profile
   *
   * @param      {Object}    req     The request
   * @param      {Object}    res     The resource
   * @param      {Function}  next    The next
   */
  updateProfile: function(req, res, next) {
    sequelize.transaction(function(t) {
      if(req.role === config.ROLE.BRAND) {
        // find this brand
        return brandService.findByUserId(req.user.userId)
          .then(function(user) {
            if(!user) {
              throw new errors.HttpStatusError(httpStatus.NOT_FOUND);
            }
            return brandService.update(req.body, user, t);
          });
      } else if(req.role === config.ROLE.INFLUENCER) {
        // find this influencer
        return influencerService.findByUserId(req.user.userId)
          .then(function(user) {
            return influencerService.update(req.body, user, t);
          });
      }
      throw new Error('role error');
    })
    .then(function(result) {
      res.send(result);
    })
    .catch(next);
  },
  listInfluencer: function(req, res, next) {
    influencerService.list(req.criteria)
    .then(function(result) {
      res.send(result);
    })
    .catch(next);
  },
  getInfluencer: function(req, res, next) {
    influencerService.findByUserId(req.params.userId)
      .then(function(user) {
        res.send(user);
      })
    .catch(next);
  },
  listBrand: function(req, res, next) {
    brandService.list(req.criteria)
    .then(function(result) {
      res.send(result);
    })
    .catch(next);
  },
  getBrand: function(req, res, next) {
    brandService.findByUserId(req.params.userId)
      .then(function(user) {
        res.send(user);
      })
    .catch(next);
  }
};
