/**
 * Handle campaign resource
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

var db = require('../models'),
  sequelize = db.sequelize,
  User = db.User,
  Campaign = db.Campaign,
  CampaignProposal = db.CampaignProposal,
  CampaignSubmission = db.CampaignSubmission;

var include = [{
  model: CampaignSubmission
}, {
  model: CampaignProposal
}, {
  model: Brand
}]

module.exports = {
  process: function(request, instance, t) {

  },
  create: function(t) {
    return Campaign.create({}, {
      include: include
    });
  },
  findAll: function(criteria) {
    // get everything
    return Campaign.findAndCountAll(criteria);
  },
  findAllByOwner: function(brandId, criteria) {
    // get only ones belong to this brand
    var opts = {
      where: { brandId: brandId }
    };

    // extend with pagination criteria
    opts = _.extend(opts, criteria);

    return Campaign.findAndCountAll(opts);
  },
  findAllByInfluencer: function(influencerId, criteria) {
    var opts = {
      include: [{
        model: CampaignProposal,
        where: {
          influencerId: influencerId
        },
        required: false
      }, {
        model: CampaignSubmission,
        where: {
          influencerId: influencerId
        },
        required: false
      }]
    };

    // extend with pagination criteria
    opts = _.extend(opts, criteria);

    return Campaign.findAndCountAll(opts);
  },
  updateWithBrandById: function(id, brandId, campaign, t) {
    var opts = {
      where: {
        campaignId: id,
        brandId: brandId
      },
      transaction: t
    };

    return Campaign.findOne(opts)
      .then(function(existingCampaign) {
        if(!existingCampaign) {
          return existingCampaign;
        }
        _.extend(existingCampaign, campaign);

        return existingCampaign.save({transaction: t});
      });
  }
};
