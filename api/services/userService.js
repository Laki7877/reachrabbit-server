/**
 * Provide user service, should be generic for influencer | brand | admin
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var db                = require('../models'),
    User              = require('../models').User,
    Brand             = require('../models').Brand,
    Influencer        = require('../models').Influencer,
    influencerService = require('../services/influencerService'),
    brandService      = require('../services/brandService');
module.exports = {
  /**
   * Find user by id, return specific form for each role (brand/influencer/admin)
   *
   * @param      {String}  id      The identifier
   */
  findById: function(id) {
    return async.parallel(
      Brand.count({ where: { userId: id } }),
      Influencer.count({ where: { userId: id} })
    )
    .then(function(results) {
      if(results[0] > 0) {
        // has brand
        return brandService.findById(id);
      } else if(results[1] > 0) {
        // has influencer
        return influencerService.findById(id);
      } else {
        // not found
        return null;
      }
    })
    .then(function(user) {
      if(user) {
        user.profilePicture.dataValues.url = process.env.S3_PUBLIC_URL + user.profilePicture.resourcePath;
      }
      return user;
    });
  }
};
