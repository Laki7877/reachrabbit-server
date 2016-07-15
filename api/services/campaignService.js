/**
 * Handle campaign resource
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

var db = require('../models'),
  Promise = require('bluebird'),
  validate = require('jsonschema').validate,
  sequelize = db.sequelize,
  User = db.User,
  Category = db.Category,
  Campaign = db.Campaign,
  CampaignProposal = db.CampaignProposal,
  CampaignSubmission = db.CampaignSubmission,
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
  update: function(instance, values, t) {
    var owner = values.user;
    var form = _.omit(values, ['user', 'state']);
    var currentState = instance.status;
    var nextState = values.status;
    var nextStateIndex = _.indexOf(states, nextState);
    var deleteStateIndex = _.indexOf(states, 'deleted');

    // state exist
    if(nextStateIndex >= 0) {
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
      // open to payment
      else if(currentState === 'open' && nextState === 'wait brand pay') {
        // check if there is at least one proposal
        return instance.getCampaignProposals({
          where: {
            status: 'propose'
          }
        });
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
