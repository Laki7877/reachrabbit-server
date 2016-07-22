/**
 * Handle submission resource
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
  Influencer = db.Influencer,
  Category = db.Category,
  Campaign = db.Campaign,
  CampaignProposal = db.CampaignProposal,
  CampaignSubmission = db.CampaignSubmission,
  PaymentTransaction = db.PaymentTransaction,
  Resource = db.Resource,
  Brand   = db.Brand;

module.exports = {
  list: function(criteria) {
    var opts = {
      where: {}
    };

    opts.where.status = criteria.status;

    _.extend(opts, criteria);

    return CampaignSubmission.findAndCountAll(opts);
  },
  findById: function(submissionId) {
    return CampaignSubmission.findById(submissionId, {
      include: [{
        model: Campaign
      }, {
        model: CampaignProposal
      }, {
        model: Influencer,
        include: [{
          model: User,
          include: [{
            model: Resource,
            as: 'profilePicture'
          }]
        }]
      }, {
        model: Resource,
        as: 'proof'
      },{
        model: Resource
      }]
    });
  },
  confirmSubmissionProofAndPay: function(values, submissionId, t) {
    return CampaignSubmission.findById(submissionId, {
      include: [CampaignProposal, {
        model: Influencer,
        include: [User]
      }]
    })
    .then(function(submission) {
      if(!submission) {
        throw new Error('no submission found');
      }

      if(submission.status !== 'posted') {
        throw new Error('is not posted yet');
      }

      if(!submission.campaignProposal) {
        throw new Error('no proposal?');
      }

      values.reference = values.reference || autogen();

      return submission.update({ status: 'paid' })
        .then(function(instance) {
          // create two payments
          // one for admin paying influencer, another for fee
          var transactionPayment = {
            status: 'complete',
            paymentType: 'transaction',
            paymentMethod: 'bank transfer',
            comment: values.comment,
            reference: values.reference,
            sourceId: config.ADMIN.USER_ID,
            targetId: instance.influencer.user.userId,
            campaignId: instance.campaignId,
            amount: instance.campaignProposal.proposePrice
          };

          var feePayment = {
            status: 'complete',
            paymentType: 'fee',
            paymentMethod: 'bank transfer',
            comment: values.comment,
            reference: values.reference,
            sourceId: instance.influencer.user.userId,
            targetId: config.ADMIN.USER_ID,
            campaignId: instance.campaignId,
            amount: instance.campaignProposal.proposePrice * config.PAYMENT.FEE
          };

          return PaymentTransaction.bulkCreate([transactionPayment, feePayment], { transaction: t })
            .then(function(instances) {
              return instances;
            });
        });
    });
  },
  createSubmissionProof: function(values, submissionId, influencerId, t) {
    if(_.isPlainObject(values)) {
      values = [values];
    }
    // find submission by influencer
    return CampaignSubmission.findOne({
      where: {
        submissionId: submissionId,
        influencerId: influencerId
      }
    }, {
      include: [{
        model: Resource,
        as: 'proof'
      }]
    })
    .then(function(submission) {
      if(!submission) {
        throw new Error('no submission found');
      }

      // should be wait for post
      if(submission.status !== 'wait for post') {
        throw new Error('cannot create proof');
      }

      // update submission to posted
      return submission.update({ status: 'posted' }, { transaction: t })
        .then(function(instance) {
          console.log(instance);
          // set proofs
          return instance.setProof(_.map(values, 'resourceId'), {transaction: t})
          .then(function() {
            return instance;
          });
        });
    });
  },
};
