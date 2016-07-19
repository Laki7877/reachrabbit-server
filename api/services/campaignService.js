/**
 * Handle campaign resource
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

var db = require('../models'),
  Promise = require('bluebird'),
  moment = require('moment'),
  validate = require('jsonschema').validate,
  sequelize = db.sequelize,
  User = db.User,
  Category = db.Category,
  Campaign = db.Campaign,
  CampaignProposal = db.CampaignProposal,
  CampaignSubmission = db.CampaignSubmission,
  PaymentTransaction = db.PaymentTransaction,
  PaymentResource = db.PaymentResource,
  Brand   = db.Brand;

// eagerload for campaign search
var include = [{
  model: CampaignSubmission
}, {
  model: CampaignProposal
}, {
  model: Brand
}, {
  model: Category
}, {
  model: PaymentTransaction
}];

// all campaign's status
var states = Campaign.rawAttributes.status.values;


// campaign on publish schema check
var campaignPublishSchema = {
  type: 'object',
  properties: {
    title: {
      type: 'string'
    },
    description: {
      type: 'string'
    },
    proposalDeadline: {
      type: 'string',
      format: 'date-time'
    },
    submissionDeadline: {
      type: 'string',
      format: 'date-time'
    }
  },
  required: ['title', 'description', 'proposalDeadline', 'submissionDeadline']
};

module.exports = {
  createProposalWithCampaign: function(values, campaignId, influencerUser, t) {
    return Campaign.findById(campaignId, {
      include: [{
        model: CampaignProposal,
        where: {
          influencerId: influencerUser.influencer.influencerId
        },
        required: false
      }]
    })
    .then(function(campaign) {
      // no campaign found
      if(!campaign) {
        console.log('no campaign, found');
        throw new Error('campaign not found');
      }

      // proposal found
      if(campaign.campaignProposal) {
        console.log('already propose');
        throw new Error('already proposed');
      }

      // check for before proposal deadline
      if(campaign.status === 'open' &&
        campaign.proposalDeadline &&
        moment(new Date()).isBefore(campaign.proposalDeadline)) {
        // get on necessary data
        var data = _.pick(values, ['title', 'description', 'proposePrice']);
        data.campaignId = campaignId;
        data.influencerId = influencerId;

        return CampaignProposal.create(data, {transaction: t});
      } else {
        console.log('deadline');
        throw new Error('deadline reached');
      }
    });
  },
  updateProposalWithCampaign: function(values, campaignId, proposalId, influencerUser, t) {
    return CampaignProposal.findOne({
      where: {
        influencerId: influencerUser.influencer.influencerId,
        campaignId: campaignId,
        proposalId: proposalId
      }
    })
    .then(function(proposal) {
      if(!proposal) {
        throw new Error('proposal not found');
      }
      return proposal.update(_.omit(values, ['influencerId', 'campaignId', 'proposalId', 'status']), { transaction: t});
    });
  },
  chooseAndPayWithCampaign: function(proposals, resource, campaignId, brandUser, t) {
    // should get campaign for this brand's
    return Campaign.findOne({
        where: {
          campaignId: campaignId,
          brandId: brandUser.brand.brandId,
          status: 'open'
        },
        include: [{
          model: CampaignProposal,
          required: false
        }, {
          model: PaymentTransaction,
          required: false
        }]
      })
      .then(function(campaign) {
        if(!campaign) {
          throw new Error('no campaign');
        }
        if(campaign.campaignProposal.length <= 0) {
          throw new Error('no campaign proposal');
        }

        if(campaign.paymentTransaction.length > 0) {
          throw new Error('payments have already been initiated');
        }

        var proposalPromises = [],
          paymentPromises = [];

        // create values for each campaign Proposal
        _.forEach(campaign.campaignProposal, function(proposal) {
          if(proposal.status === 'accept') {
            throw new Error('proposal already accept but untransaction? something is wrong');
          }
          var acceptance = _.findIndex(proposals, function(e) { return e.proposalId === proposal.proposalId }) >= 0;

          if(acceptance) {
            // update proposal
            proposalPromises.push(proposal.update({ status: 'accept' }, { transaction: t }));

            // create each payment
            paymentPromises.push(PaymentTransaction.create({
              userId: brandUser.userId,
              resourceId: resource.resourceId,
              campaignId: campaign.campaignId,
              paymentType: 'receive',
              paymentMethod: 'bank transfer',
              value: proposal.proposalPrice
            }, {
              transaction: t,
              include: [{
                model: PaymentTransaction,
                through: {
                  model: PaymentResource
                }
              }]
            }));
          }
        });

        return Promise.all([
          proposalPromises, // update accepted proposal status
          paymentPromises // create payment transaction
        ])
        .then(function() {
          return campaign;
        });
      });
  },
  update: function(instance, values, t) {
    var owner = values.user;
    var form = _.omit(values, ['user', 'status']);
    var currentState = instance.status;
    var nextState = _.get(values, 'status', currentState);
    var nextStateIndex = _.indexOf(states, nextState);
    var deleteStateIndex = _.indexOf(states, 'delete');

    if(currentState === nextState) {
      // Save/Update as draft
      if(currentState === 'draft') {
        _.extend(instance, values);
        return instance.save({transaction: t});
      }
    }
    // state exist
    else if(nextStateIndex >= 0) {
      // draft to open
      if(currentState === 'draft' && nextState === 'open') {
        var data = _.extend({}, instance.dataValues, values);

        // check required field for publish
        if(validate(data, campaignPublishSchema)) {
          // update and save
          _.extend(instance, values);
          return instance.save({transaction: t});
        } else {
          // field not meet requirement
          return Promise.reject(new Error('field error'));
        }
      }
    }

    return Promise.reject(new Error('something is wrong'));
  },
  create: function(values, t) {
    // build data
    if(!values.brand) {
      return Promise.reject('no brand owner');
    }

    var brand = values.brand;
    var category = values.category;
    var data = _.extend(_.omit(values, ['brand', 'category']), {
      status: 'draft' // default status
    });

    // find category for this campaign
    return Promise.all([
      Brand.findById(brand.brandId),
      Category.findOne({ where: category })
    ]).spread(function(brandInstance, categoryInstance) {
      if(!categoryInstance || !brandInstance) {
        throw new Error('brand or category not found');
      }
      data.categoryId = categoryInstance.categoryId;
      data.brandId = brandInstance.brandId;

      return Campaign.create(data, { include: include, transaction: t});
    });
  },
  list: function(criteria) {
    // get everything
    return Campaign.findAndCountAll(criteria);
  },
  listByOwner: function(brandId, criteria) {
    // get only ones belong to this brand
    var opts = {
      where: { brandId: brandId }
    };

    // extend with pagination criteria
    opts = _.extend(opts, criteria);

    return Campaign.findAndCountAll(opts);
  },
  listByInfluencer: function(influencerId, criteria) {
    // all
    var opts = {
      where: {},
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

    // influencer status
    var status = null;

    // get ones which u applied
    if(criteria.filter === 'applied') {
      opts.where.status = 'open';
      opts.include[0].required = true;
    }
    // get ones which ur producing
    if(criteria.filter === 'production') {
      opts.where.status = 'production';
      opts.include[1].required = true;
    }
    // get ones that are completed
    if(criteria.filter === 'complete') {
      opts.where.status = 'complete';
      opts.include[1].required = true;
    }
    // extend with pagination criteria
    opts = _.extend(opts, criteria);

    return Campaign.findAndCountAll(opts);
  },
  findById: function(id) {
    return Campaign.findById(id, {
      include: include
    });
  },
  findByIdWithBrand: function(id, brandId) {
    return Campaign.findOne({
      where: {
        campaignId: id,
        brandId: brandId
      },
      include: include
    });
  }
};
