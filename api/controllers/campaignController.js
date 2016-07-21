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
  payForCampaign: function(req, res, next) {
    var form = req.body;

    sequelize.transaction(function(t) {
      return campaignService.pay(form, req.params.campaignId, req.user.brand.brandId, t);
    })
    .then(function(result) {
      return result.reload();
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  },
  readyToPayForCampaign: function(req, res, next) {
    sequelize.transaction(function(t) {
      return campaignService.readyToPay(req.params.campaignId, req.user.brand.brandId, t);
    })
    .then(function(result) {
      return result.reload();
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  },
  createSubmission: function(req, res, next) {
    var form = req.body;

    sequelize.transaction(function(t) {
      return campaignService.createSubmission(form, req.params.campaignId, req.user, t);
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
      return campaignService.createProposal(form, req.params.campaignId, req.user.influencer.influencerId, t);
    })
    .then(function(result) {
      return result.reload();
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  },
  updateProposal: function(req, res, next) {
    var form = req.body;

    sequelize.transaction(function(t) {
      return campaignService.updateProposal(form, req.params.campaignId, req.params.proposalId, req.user.brand.brandId, t);
    })
    .then(function(result) {
      return result.reload();
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  },
  updateSubmission: function(req, res, next) {
    var form = req.body;

    sequelize.transaction(function(t) {
      return campaignService.updateSubmission(form, req.params.campaignId, req.params.submissionId, req.user.brand.brandId, t);
    })
    .then(function(result) {
      return result.reload();
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  },
  createCampaign: function(req, res, next) {
    var form = req.body;

    sequelize.transaction(function(t) {
      return campaignService.create(_.extend(form, {brand: req.user.brand}), t);
    })
    .then(function(result) {
      return result.reload();
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  },
  updateCampaign: function(req, res, next) {
    var campaignId = req.params.campaignId;
    var form = req.body;

    sequelize.transaction(function(t) {
      return campaignService.update(form, req.params.campaignId, req.user.brand.brandId, t);
    })
    .then(function(result) {
      return result.reload();
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  },
  listSubmission: function(req, res, next) {
    Promise.try(function() {
      if(req.role === config.ROLE.BRAND) {
        return campaignService.listInfluencerWithSubmission(req.params.campaignId, req.user.brand.brandId, req.criteria);
      } else if(req.role === config.ROLE.INFLUENCER){
        return campaignService.listSubmissionByInfluencerId(req.params.campaignId, req.user.influencer.influencerId, req.criteria);
      }
    })
    .then(function(result) {
        return res.send(result);
      })
    .catch(next);
  },
  listProposal: function(req, res, next) {
    Promise.try(function() {
      if(req.role === config.ROLE.BRAND) {
        return campaignService.listInfluencerWithProposal(req.params.campaignId, req.user.brand.brandId, req.criteria);
      } else if(req.role === config.ROLE.INFLUENCER){
        return campaignService.listProposalByInfluencerId(req.params.campaignId, req.user.influencer.influencerId, req.criteria);
      }
    })
    .then(function(result) {
        return res.send(result);
      })
    .catch(next);
  },
  listCampaign: function(req, res, next) {
    campaignService.list(_.extend(req.criteria, req.query))
      .then(function(result) {
        return res.send(result);
      })
      .catch(next);
  },
	listCampaignByRole: function(req, res, next) {
    Promise.attempt(function() {
      _.extend(req.criteria, req.query);
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
  listTransaction: function(req, res, next) {
    return campaignService.listTransaction(req.params.campaignId, req.user.brand.brandId, req.criteria)
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  },
  getCampaign: function(req, res, next) {
    campaignService.findById(req.params.campaignId)
      .then(function(result) {
        return res.send(result);
      })
      .catch(next);
  },
  confirmCampaignPayment: function(req, res, next) {
    sequelize.transaction(function(t) {
      return campaignService.confirmCampaignPayment(req.params.campaignId, t);
    })
    .then(function(campaign) {
      return campaign.reload();
    })
    .then(function(campaign) {
      return res.send(campaign);
    })
    .catch(next);
  }
};
