/**
 * Handle payment resource
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
  Resource = db.Resource,
  Brand   = db.Brand;

var include = [{
  model: User,
  as: 'source'
}, {
  model: User,
  as: 'target'
}, {
  model: Resource
}, {
  model: Campaign
}];

module.exports = {
  list: function(criteria) {
    var opts = {
      include: include
    };

    _.extend(opts, criteria);
    return PaymentTransaction.findAndCountAll(opts);
  },
  findById: function(transactionId) {
    return PaymentTransaction.findById(transactionId, {
      include: include
    });
  },
  findByIdWithUserId: function(transactionId, userId) {
    return PaymentTransaction.findOne({
      where: {
        $or: [{ sourceId: userId }, { targetId: userId }]
      },
      include: include
    })
    .then(function(result) {
      if(!result) {
        throw new errors.HttpStatusError(httpStatus.NOT_FOUND, 'Transaction not found');
      }
      return result;
    });
  },
  updateAll: function(arrayOfPayments, t) {
    return PaymentTransaction.findAll({
      where: {
        transactionId: {
          $in: _.map(arrayOfPayments, function(e) { return e.transactionId })
        }
      }
    })
    .then(function(results) {
      var promises = [];
      _.forEach(results, function(instance) {
        var payment = _.find(arrayOfPayments, function(e) { return e.transactionId === instance.transactionId });
        promises.push(instance.update(payment, { transaction: t }));
      });

      return Promise.all(promises);
    });
  },
  listByUserId: function(userId, criteria) {
    var opts = {
      where: {
        userId: userId
      },
      include: include
    };

    _.extend(opts, criteria);
    return PaymentTransaction.findAndCountAll(opts);
  },
  listByCampaignId: function(campaignId, criteria) {
    var opts = {
      where: {
        campaignId: campaignId
      },
      include: include
    };

    _.extend(opts, criteria);
    return PaymentTransaction.findAndCountAll(opts);
  }
};
