/**
 * Handle campaign
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @fixer      Pat Sabpisa <ssabpisa@me.com>
 * @since      0.0.3
 */
'use strict';

var config = require('config'),
  db = require('../models'),
  sequelize = db.sequelize,
	campaignService = require('../services/campaignService');

module.exports = {
  create: function(req, res, next) {
    var form = req.body;

    sequelize.transaction(function(t) {
      var campaign = campaignService.build(form, null, t);

      campaign.addBrand(req.user.Brand);
      return campaign.save({transaction: t});
    })
    .catch(next);
  },
  update: function(req, res, next) {
    var brandId = req.user.Brand.brandId;
    var campaignId = req.params.id;
    var data = req.body;

    sequelize.transaction(function(t) {
      return campaignService.update(campaignId, brandId, data, t)
        .then(function(campaign) {
          if(!campaign) {
            throw new errors.HttpStatusError(httpStatus.NOT_FOUND);
          }
          return res.send(campaign);
        });
    })
    .catch(next);
  },
	list: function(req, res, next) {
		if(req.role === config.ROLE.BRAND) {
      // find by brand owner
			return campaignService.findAllByOwner(req.user.Brand.brandId, req.criteria)
				.then(function(campaigns) {
					res.send(campaigns);
				})
				.catch(next);
		} else if(req.role === config.ROLE.INFLUENCER) {
      // find by influencers
      return campaignService.findAllByInfluencer(req.user.Influencer.influencerId, req.criteria)
        .then(function(campaigns) {
          res.send(campaigns);
        })
        .catch(next);
    } else if(req.role === config.ROLE.ADMIN) {
      // find all for admin-only
      return campaignService.findAll(req.criteria)
        .then(function(campaigns){
          res.send(campaigns);
        })
        .catch(next);
    } else {
      // no role?
      next(new errors.HttpStatusError(httpStatus.NOT_IMPLEMENTED));
    }
	}
};
