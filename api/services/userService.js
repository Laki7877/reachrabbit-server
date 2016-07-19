/**
 * Provide user service, should be generic for influencer | brand | admin
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var Promise           = require('bluebird'),
    authHelper        = require('../helpers/authHelper'),
    db                = require('../models'),
    User              = require('../models').User,
    Brand             = require('../models').Brand,
    Influencer        = require('../models').Influencer,
    Resource          = require('../models').Resource,
    influencerService = require('../services/influencerService'),
    brandService      = require('../services/brandService');

var include = [{
  model: Brand,
  required: false
},{
  model: Resource,
  as: 'profilePicture'
}];

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
        return brandService.findByUserId(id);
      } else if(results[1] > 0) {
        // has influencer
        return influencerService.findByUserId(id);
      } else {
        // not found, maybe admin
        return User.findById(id)
          .then(function(user) {
            if(!user) {
              return null;
            }
          });
      }
    });
  },
  findByEmail: function(email) {
    return User.findOne({
      where: {
        email: email
      },
      include: include
    });
  },
  createToken: function(user, role, cache) {
    return authHelper.createToken(user, role, cache);
  }
};
