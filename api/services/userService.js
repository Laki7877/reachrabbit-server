/**
 * Provide user service, should be generic for influencer | brand | admin
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var Promise           = require('bluebird'),
    db                = require('../models'),
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
    return Promise.all([
      Brand.count({ where: { userId: id } }),
      Influencer.count({ where: { userId: id} })
    ])
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
    });
  },
  findByEmail: function(email,includes) {
    if(!includes){
      includes = [];
    }
    if(!Array.isArray(includes)){
      includes = [includes];
    }
    return User.findOne({
      where: {
        email: email
      },
      include: includes
    });
  }
};
