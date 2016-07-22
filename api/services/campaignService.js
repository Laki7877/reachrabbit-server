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
  config = require('config'),
  validate = require('jsonschema').validate,
  sequelize = db.sequelize,
  User = db.User,
  Category = db.Category,
  Influencer = db.Influencer,
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
  include: [{model: Resource},{
    model: Influencer,
    include: {
      model: User,
      include: [
        {
          model: Resource,
          as: 'profilePicture'
        }
      ]
    }
  }]
}, {
  model: CampaignProposal,
  include: [{
     model: Resource
  },{
    model: Influencer,
    include: [{
      model: User,
      include: [
        {
          model: Resource,
          as: 'profilePicture'
        }
      ]
    }]
  }]
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
      var promises = [];
      _.forEach(campaign.paymentTransactions, function(payment) {
        promises.push(payment.update({status: 'complete'}, {transaction: t}));
      });

      return Promise.all(promises)
        .then(function() {
          return campaign.update({status: 'production'}, {transaction: t});
        });
    });
  },
  createSubmission: function(values, campaignId, influencerId, t) {
    return Campaign.findOne({
        include: [{
          model: CampaignProposal,
          where: {
            influencerId: influencerId
          }
        }],
        where: {
          campaignId: campaignId
        },
        order: [[ CampaignProposal, 'createdAt', 'DESC' ]]
      })
      .then(function(campaign) {
        if(!campaign) {
          throw new Error('campaign not found');
        }

        // console.log(campaign);
        if(campaign.campaignProposals.length <= 0) {
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
          data.proposalId = campaign.campaignProposals[0].proposalId;

          return CampaignSubmission.create(data, {include: [Resource], transaction: t })
            .then(function(instance) {
              if(resources) {
                return instance.setResources(_.map(resources, 'resourceId'), {transaction: t})
                  .then(function() {
                    return instance;
                  });
              } else {
                return instance;
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
              if(resources) {
                return instance.setResources(_.map(resources, 'resourceId'), {transaction: t})
                  .then(function() {
                    return instance;
                  });
              } else {
                return instance;
              }
            });
        } else {
          throw new Error('deadline reached');
        }
      });
  },
  listProposalByInfluencerId: function(campaignId, influencerId, criteria) {
    var opts = {
      include: [{
        model: Campaign,
        where: {
          campaignId: campaignId
        }
      }, {
        model: Influencer,
        where: {
          influencerId: influencerId
        },
        include: [{
          model: User
        }]
      }],
      order: 'createdAt DESC'
    };

    _.merge(opts, criteria);

    return CampaignProposal.findAndCountAll(opts);
  },
  listSubmissionByInfluencerId: function(campaignId, influencerId, criteria) {
    var opts = {
      include: [{
        model: Campaign,
        where: {
          campaignId: campaignId
        }
      }, {
        model: Influencer,
        where: {
          influencerId: influencerId
        },
        include: [{
          model: User
        }]
      }],
      order: 'createdAt DESC'
    };

    _.merge(opts, criteria);

    return CampaignSubmission.findAndCountAll(opts);
  },
  listInfluencerWithSubmission: function(campaignId, brandId, criteria) {
    var opts = {
      include: [{
        model: Resource,
        as: "profilePicture"
      },{
        model: Influencer,
        include: [{
          model: CampaignSubmission,
          include: [{ model: Resource}, {
            model: Campaign,
            where: {
              campaignId: campaignId,
              brandId: brandId
            },
            required: true
          }],
          required: true
        }],
        required: true
      }],
      order: [
        [ Influencer, CampaignSubmission, 'createdAt', 'DESC' ]
      ]
    };

    // _.merge(opts, criteria);

    return User.findAndCountAll(opts);
  },
  listInfluencerWithProposal: function(campaignId, brandId, influencerId, criteria) {
    var opts = {
      order: [
        [ Influencer, CampaignProposal, 'createdAt', 'DESC' ]
      ],
      include: [{
        model: Resource,
        as: "profilePicture"
      },{
        model: Influencer,
        include: [{
          model: CampaignProposal,
          where: {
            campaignId: campaignId
          },
          include: [Resource, {
            model: Campaign,
            where: {
              brandId: brandId
            },
            required: true
          }],
          required: true
        }],
        required: true
      }]
    };

    // _.merge(opts, criteria);

    return User.findAndCountAll(opts);
  },
  updateSubmission: function(values, campaignId, submissionId, brandId, t) {
    // find submission by campaign with brand
    return CampaignSubmission.findOne({
      where: {
        campaignId: campaignId,
        submissionId: submissionId
      },
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

      // if(values.status === 'need revision') {
      //   values = _.pick(values, ['status', 'comment']);
      // } else {
      //   values = _.pick(values, ['status', 'comment']);
      // }

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
      },
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
      if(proposal.campaign.status !== 'open') {
        throw new Error('cannot update due to campaign status');
      }
      var data = _.pick(values, ['comment', 'isSelected', 'status']);
      return proposal.update(data, {transaction: t});
    });
  },
  readyToPay: function(campaignId, brandId, t) {
    return Campaign.findOne({
      where: {
        campaignId: campaignId,
        brandId: brandId
      },
      include: [{
        model: CampaignProposal,
        where: {
          isSelected: true
        }
      }, {
        model: PaymentTransaction
      },{
        model: Brand,
        include: [
        {model: User}
        ]
      }]
    })
    .then(function(campaign) {
      if(!campaign) {
        throw new Error('no campaign');
      }
      if(campaign.paymentTransactions.length > 0) {
        throw new Error('payment already made');
      }

      var paymentPromises = [];

      if(campaign.campaignProposals.length >= 0) {
        _.forEach(campaign.campaignProposals, function(proposal) {
          paymentPromises.push(PaymentTransaction.create({
            sourceId: campaign.brand.user.userId,
            targetId: config.ADMIN.USER_ID,
            campaignId: campaign.campaignId,
            paymentType: 'transaction',
            paymentMethod: 'bank transfer',
            amount: proposal.proposePrice,
            status: 'pending'
          }, {
            transaction: t
          }));
        });
      }
      else {
        throw new Error('no proposal selected!');
      }

      return campaign.update({status: 'payment pending'}, {transaction: t})
        .then(function() {
          return Promise.all(paymentPromises);
        });
    });
  },
  pay: function(values, campaignId, brandId, t) {
    return Campaign.findOne({
      where: {
        campaignId: campaignId,
        brandId: brandId
      },
      include: [{
        model: PaymentTransaction
      }]
    })
    .then(function(campaign) {
      if(campaign.paymentTransactions <= 0) {
        throw new Error('no transaction');
      }

      var promises = [];

      _.forEach(campaign.paymentTransactions, function(payment) {
        promises.push(payment.setResource(values.resourceId, {transaction: t}));
      });

      return campaign.update({status: 'wait for confirm'}, {transaction: t})
        .then(function() {
          return Promise.all(promises);
        })
        .then(function(results) {
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
      },
      include: include,
      order: [
        [ CampaignProposal, 'createdAt', 'DESC' ],
        [ CampaignSubmission, 'createdAt', 'DESC' ]
      ]
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

      if(currentState === nextState || !nextState) {
        // Save/Update as draft
        if(currentState === 'draft') {
          _.extend(instance, values);
          if(values.category) {
            instance.categoryId = values.category.categoryId;
          }
          return instance.save({transaction: t})
          .then(function(instance) {
            var resources = instance.resources || values.resources;
            var media = instance.media || values.media;
            return Promise.all([
              instance.setResources(_.map(resources, 'resourceId'), { transaction: t }),
              instance.setMedia(_.map(media, 'mediaId'), { transaction: t })
            ])
            .then(function() {
              return instance;
            });
          });
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
  listOpenInfluencer: function(criteria, influencerId) {
    var opts = {
      include: [
      {
        model: CampaignProposal,
        where: {
          influencerId: influencerId
        },
        required: false
      }, {
        model: Resource,
        required: false
      },{
        model: Brand,
        include: [User],
        required: false
      }, {
        model: Media,
        required: false
      }],
      where: {
        '$campaignProposals.proposalId$': null,
        status: 'open'
      },
      order: [
        [ CampaignProposal, 'createdAt', 'DESC' ]
      ]
    };

    //opts.where.status = 'open';
    //_.merge(opts, criteria);
    // _.merge(opts, criteria);

    return Campaign.count(opts)
    .then(function(count) {
      return Campaign.findAll(opts)
        .then(function(rows) {
          rows = _(rows).slice(criteria.offset).take(criteria.limit).value();
          return {
            count: count,
            rows: rows
          };
        });
    });
  },
  list: function(criteria, extopts) {
    var opts = {
      include: [{
        model: Resource
      },{
        model: CampaignProposal
      },{
        model: Brand,
        include: [User]
      }, {
        model: Media,
        required: false
      }],
      order: [
        [ CampaignProposal, 'createdAt', 'DESC' ]
      ]
    };
    _.merge(opts, extopts, criteria);

    return Campaign.findAndCountAll(opts);
  },
  listByOwner: function(brandId, criteria) {
    // get only ones belong to this brand
    var opts = {
      where: { brandId: brandId },
      include: include,
      order: [
        [ CampaignProposal, 'createdAt', 'DESC' ],
        [ CampaignSubmission, 'createdAt', 'DESC' ]
      ]
    };

    _.merge(opts, criteria);

    return Campaign.findAndCountAll(opts);
  },
  listByInfluencer: function(influencerId, criteria) {
    // all
    var opts = {
      where: {},
      include: [{
        model: Resource
      },{
        model: CampaignProposal,
        where: {
          influencerId: influencerId
        },
        required: true
      }, {
        model: CampaignSubmission
      }, {
        model: Media
      }],
      order: [
        [ CampaignProposal, 'createdAt', 'DESC' ],
        [ CampaignSubmission, 'createdAt', 'DESC' ]
      ]
    };

    // extend with pagination criteria
    //opts = _.extend(opts, criteria);

    return Campaign.count(opts)
    .then(function(count) {
      return Campaign.findAll(opts)
        .then(function(rows) {
          rows = _.compact(_.map(rows, function(e) {
            if(e.status !== 'open' &&
                _.findIndex(e.campaignProposals, function(r) {
                  return r.isSelected
                }) < 0) {
              return null;
            }
            else {
              return e;
            }
          }));
          rows = _(rows).slice(criteria.offset).take(criteria.limit).value();
          return {
            count: count,
            rows: rows
          };
        });
    });
  },
  findById: function(id) {
    return Campaign.findOne({
      where: {
        campaignId: id
      },
      include: include,
      order: [
        [ CampaignProposal, 'createdAt', 'DESC' ],
        [ CampaignSubmission, 'createdAt', 'DESC' ]
      ]
    });
  },
  findByIdWithInfluencer: function(id, influencerId){

      var include = [{
        model: CampaignSubmission,
        order: 'createdAt DESC',
        include: [{
          model: Resource
        }]
      }, {
        model: CampaignProposal,
        where: {
          influencerId: influencerId
        },
        include: [{
          model: Influencer,
          include: {
            model: User
          }
        },{
          model: Resource
        }],
        order: 'createdAt DESC'
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

     return Campaign.findOne({
      where: {
        campaignId: id
      },
      include: include,
      order: [
        [ CampaignProposal, 'createdAt', 'DESC' ],
        [ CampaignSubmission, 'createdAt', 'DESC' ]
      ]
    });
  },
  findByIdWithBrand: function(id, brandId) {
    return Campaign.findOne({
      where: {
        campaignId: id,
        brandId: brandId
      },
      include: include,
      order: [
        [ CampaignProposal, 'createdAt', 'DESC' ],
        [ CampaignSubmission, 'createdAt', 'DESC' ]
      ]
    });
  }
};
