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
  Resource = db.Resource,
  Media = db.Media,
  CampaignProposal = db.CampaignProposal,
  CampaignSubmission = db.CampaignSubmission,
  PaymentTransaction = db.PaymentTransaction,
  PaymentResource = db.PaymentResource,
  Brand   = db.Brand;

// eagerload for campaign search
var include = [{
  model: CampaignSubmission,
  order: 'createdDt DESC'
}, {
  model: CampaignProposal,
  order: 'createdDt DESC'
}, {
  model: Media
}, {
  model: Resource
}, {
  model: Brand,
  include: [User]
}, {
  model: Category
}, {
  model: PaymentTransaction,
  include: [Resource]
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

var autogen = function() {
  return Math.random().toString(36).substr(2, 9);
};

module.exports = {
  confirmCampaignPayment: function(campaignId, t) {
    return Campaign.findById(campaignId, {
      include: [{
        model: PaymentTransaction
      }]
    })
    .then(function(campaign) {
      if(campaign.paymentTransaction.length <= 0) {
        throw new Error('payment not found');
      }

      var promises = [];
      _.forEach(campaign.paymentTransaction, function(payment) {
        if(payment.status === 'complete') {
          throw new Error('payment already confirmed?');
        }
        promises.push(payment.update({status: 'complete'}, {transaction: t}));
      });

      return Promise.all(promises)
        .then(function() {
          return campaign.update({status: 'production'}, {transaction: t});
        });
    });
  },
  createSubmission: function(values, campaignId, influencerId, t) {
    return Campaign.findById(campaignId, {
        include: [{
          model: CampaignProposal,
          where: {
            influencerId: influencerId
          },
          order: 'createdDt DESC'
        }]
      })
      .then(function(campaign) {
        if(!campaign) {
          throw new Error('campaign not found');
        }

        if(campaign.campaignProposal.length <= 0) {
          throw new Error('you havent propose yet!');
        }

        // check for before submission deadline
        if(campaign.status === 'production' &&
            campaign.submissionDeadline &&
            moment().isBefore(campaign.submissionDeadline)) {
          var data = _.pick(values, ['title', 'description']);
          var resources = values.resources;
          data.campaignId = campaignId;
          data.influencerId = influencerId;
          data.proposalId = campaign.campaignProposal[0].proposalId;

          return CampaignSubmission.create(data, { include: [Resource], transaction: t })
            .then(function(instance) {
              if(resource) {
                return instance.setResources(_.map(resources, 'resourceId'), {transaction: t})
                  .then(function() {
                    return instance;
                  });
              }
            });
        } else {
          throw new Error('deadline reached');
        }
      });
  },
  createProposal: function(values, campaignId, influencerId, t) {
    return Campaign.findById(campaignId)
      .then(function(campaign) {
        // no campaign found
        if(!campaign) {
          console.log('no campaign, found');
          throw new Error('campaign not found');
        }

        // check for before proposal deadline
        if(campaign.status === 'open' &&
          campaign.proposalDeadline &&
          moment().isBefore(campaign.proposalDeadline)) {
          // get on necessary data
          var data = _.pick(values, ['title', 'description', 'proposePrice']);
          var resources = values.resources;
          data.campaignId = campaignId;
          data.influencerId = influencerId;

          return CampaignProposal.create(data, { include: [Resource], transaction: t})
            .then(function(instance) {
              if(resource) {
                return instance.setResources(_.map(resources, 'resourceId'), {transaction: t})
                  .then(function() {
                    return instance;
                  });
              }
            });
        } else {
          console.log('deadline');
          throw new Error('deadline reached');
        }
      });
  },
  listSubmission: function(campaignId, brandId, criteria) {
    var opts = {
      where: {
        campaignId: campaignId
      },
      include: [{
        model: Campaign,
        where: {
          brandId: brandId
        }
      }]
    };

    _.extend(opts, criteria);

    return CampaignSubmission.findAndCountAll(opts);
  },
  listProposal: function(campaignId, brandId, criteria) {
    var opts = {
      where: {
        campaignId: campaignId
      },
      include: [{
        model: Campaign,
        where: {
          brandId: brandId
        }
      }]
    };

    _.extend(opts, criteria);

    return CampaignProposal.findAndCountAll(opts);
  },
  updateSubmission: function(values, campaignId, submissionId, brandId, t) {
    // find submission by campaign with brand
    return CampaignSubmission.findOne({
      where: {
        campaignId: campaignId,
        submissionId: submissionId
      }
    }, {
      include: [{
        model: Campaign,
        where: {
          brandId: brandId
        }
      }]
    })
    .then(function(submission) {
      if(!submission) {
        throw new Error('submission not found');
      }

      if(submission.status !== 'wait for review') {
        throw new Error('not in approvable status');
      }

      if(values.status === 'need revision') {
        values = _.pick(values, ['status', 'comment']);
      } else {
        values = _.pick(values, ['status']);
      }
      return submission.update(values, {transaction: t});
    });
  },
  updateProposal: function(values, campaignId, proposalId, brandId, t) {
    if(!brandId) {
      brandId = undefined;
    }
    return CampaignProposal.findOne({
      where: {
        campaignId: campaignId,
        proposalId: proposalId
      }
    }, {
      include: [{
        model: Campaign,
        where: {
          brandId: brandId
        }
      }]
    })
    .then(function(proposal) {
      if(!proposal) {
        throw new Error('proposal not found');
      }
      if(submission.campaign.status !== 'open') {
        throw new Error('cannot update due to campaign status');
      }
      var data = _.pick(values, ['comment', 'status']);
      return proposal.update(data, {transaction: t});
    });
  },
  chooseProposalAndPay: function(proposals, resource, campaignId, brandUser, t) {
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

            // create brand paying payment
            paymentPromises.push(PaymentTransaction.create({
              sourceId: brandUser.sourceId,
              targetId: config.ADMIN.USER_ID,
              resourceId: resource.resourceId,
              campaignId: campaign.campaignId,
              paymentType: 'transaction',
              paymentMethod: 'bank transfer',
              amount: proposal.proposalPrice,
              status: 'pending'
            }, {
              transaction: t,
              include: [{
                model: PaymentTransaction
              }]
            }));
          }
        });

        return Promise.all([
          Promise.all(proposalPromises), // update accepted proposal status
          Promise.all(paymentPromises) // create payment transaction
        ])
        .then(function() {
          return campaign;
        });
      });
  },
  listTransaction: function(campaignId, brandId, criteria) {
    var opts = {
      where: {
        campaignId: campaignId
      },
      include: [{
        model: Campaign,
        where: {
          brandId: brandId
        }
      }]
    };

    _.extend(opts, criteria);

    return PaymentTransaction.findAndCountAll(opts);
  },
  update: function(values, campaignId, brandId, t) {
    return Campaign.findOne({
      where: {
        brandId: brandId,
        campaignId: campaignId
      }
    }, {
      include: include
    })
    .then(function(instance) {
      if(!instance) {
        throw new Error('campaign not found');
      }
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
            throw new Error('field error');
          }
        }
      }

      throw new Error('something is wrong');
    });
  },
  create: function(values, t) {
    // build data
    if(!values.brand) {
      return Promise.reject('no brand owner');
    }

    var brand = values.brand;
    var category = values.category;
    var media = values.media;
    var resources = values.resources;
    var data = _.extend(_.omit(values, ['brand', 'category', 'media', 'resources']), {
      status: 'draft' // default status
    });

    // find category for this campaign
    return Promise.all([
      Brand.findById(brand.brandId),
      Category.findOne({ where: category })
    ])
    .spread(function(brandInstance, categoryInstance) {
      if(!categoryInstance || !brandInstance) {
        throw new Error('brand or category not found');
      }
      data.categoryId = categoryInstance.categoryId;
      data.brandId = brandInstance.brandId;

      return Campaign.create(data, { include: include, transaction: t})
        .then(function(instance) {
          return Promise.all([
            instance.setResources(_.map(resources, 'resourceId'), { transaction: t }),
            instance.setMedia(_.map(media, 'mediaId'), { transaction: t })
          ])
          .then(function() {
            return instance;
          });
        });
    });
  },
  list: function(criteria, extopts) {
    var opts = {
      where: {},
      include: [{
        model: Brand,
        include: [User]
      }, {
        model: Media,
        where: {},
        required: false
      }]
    };

    _.merge(opts, extopts, criteria);

    // filter by status name
    if(criteria.status) {
      opts.where.status = criteria.status;
    }
    // filter by media provider name
    if(criteria.media) {
      opts.include[1].where.mediaId = criteria.media;
    }

    return Campaign.findAndCountAll(opts);
  },
  listByOwner: function(brandId, criteria) {
    // get only ones belong to this brand
    var opts = {
      where: { brandId: brandId }
    };
    return this.list(criteria, opts);
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
    if(criteria.status === 'applied') {
      opts.where.status = 'open';
      opts.include[0].required = true;
    }
    // get ones which ur producing
    if(criteria.status === 'production') {
      opts.where.status = 'production';
      opts.include[1].required = true;
    }
    // get ones that are completed
    if(criteria.status === 'complete') {
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
