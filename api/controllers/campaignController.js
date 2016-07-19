/**
 * Handle campaign
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @fixer      Pat Sabpisa <ssabpisa@me.com>
 * @since      0.0.3
 */
'use strict';

var config = require('config'),
  Promise = require('bluebird'),
  db = require('../models'),
  sequelize = db.sequelize,
	campaignService = require('../services/campaignService');

module.exports = {
  createPayment: function(req, res, next) {
    var form = req.body;

    sequelize.transaction(function(t) {
      return campaignService.chooseAndPayWithCampaign(form.campaignProposal, form.resource, req.params.campaignId, req.user, t);
    })
    .then(function(result) {
      return result.reload();
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  },
  createProposal: function(req, res, next) {
    var form = req.body;

    sequelize.transaction(function(t) {
      return campaignService.createProposalWithCampaign(form, req.params.campaignId, req.user, t);
    })
    .then(function(result) {
      return res.send
    })
    .catch(next);
  },
  updateProposal: function(req, res, next) {
    var form = req.body;

    sequelize.transaction(function(t) {
      return campaignService.updateProposalWithCampaign(form, req.params.campaignId, req.params.proposalId, req.user, t);
    })
    .then(function(result) {
      return result.reload();
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  },
  create: function(req, res, next) {
    var form = req.body;

    sequelize.transaction(function(t) {
      return campaignService.create(_.extend(form, {brand: req.user.brand}), t);
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  },
  update: function(req, res, next) {
    var campaignId = req.params.id;
    var form = req.body;

    sequelize.transaction(function(t) {
      return campaignService.findByIdWithBrand(campaignId, req.user.brand.brandId)
      .then(function(campaign) {
        return campaignService.update(campaign, form, t);
      });
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  },
  list: function(req, res, next) {
    campaignService.list(req.criteria)
      .then(function(result) {
        return res.send(result);
      })
      .catch(next);
  },
	listByRole: function(req, res, next) {
    Promise.attempt(function() {
      if(req.role === config.ROLE.BRAND) {
        // find by brand owner
  			return campaignService.listByOwner(req.user.brand.brandId, req.criteria);
  		} else if(req.role === config.ROLE.INFLUENCER) {
        // find by influencers
        return campaignService.listByInfluencer(req.user.influencer.influencerId, req.criteria);
      } else if(req.role === config.ROLE.ADMIN) {
        // find all for admin-only
        return campaignService.list(req.criteria);
      } else {
        // no role?
        throw new errors.HttpStatusError(httpStatus.NOT_IMPLEMENTED);
      }
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
	},
  get: function(req, res, next) {
    campaignService.findById(req.params.campaignId)
      .then(function(result) {
        return res.send(result);
      })
      .catch(next);
  }
};
