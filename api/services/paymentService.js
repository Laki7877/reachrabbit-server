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
  Brand   = db.Brand;

module.exports = {
  list: function(criteria) {
    var opts = {
      include: [{
        model: Campaign
      }, {
        model: User
      }]
    };

    _.extend(opts, criteria);
    return PaymentTransaction.findAndCountAll(opts);
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
      include: [{
        model: User
      }, {
        model: Resource
      }, {
        model: Campaign
      }]
    };

    _.extend(opts, criteria);
    return PaymentTransaction.findAndCountAll(opts);
  },
  listByCampaignId: function(campaignId, criteria) {
    var opts = {
      where: {
        campaignId: campaignId
      },
      include: [{
        model: User
      }, {
        model: Resource
      }]
    };

    _.extend(opts, criteria);
    return PaymentTransaction.findAndCountAll(opts);
  }
};
