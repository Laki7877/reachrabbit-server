/**
 * Handle payments
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

var brandService = require('../services/brandService'),
    influencerService = require('../services/influencerService'),
    userService = require('../services/userService'),
    submissionService = require('../services/submissionService'),
    sequelize = require('../models').sequelize,
    config = require('config');


module.exports = {
  listSubmission: function(req, res, next) {
    submissionService.list(req.criteria)
      .then(function(result) {
        return res.send(result);
      })
      .catch(next);
  },
  getSubmission: function(req, res, next) {
    submissionService.findById(req.params.submissionId)
      .then(function(result) {
        return res.send(result);
      })
      .catch(next);
  },
  createSubmissionProof: function(req, res, next) {
    var data = req.body;
    sequelize.transaction(function(t) {
      return submissionService.createSubmissionProof(data, req.params.submissionId, req.user.influencer.influencerId, t);
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  },
  confirmSubmissionProof: function(req, res, next) {
    var data = req.body;
    sequelize.transaction(function(t) {
      return submissionService.confirmSubmissionAndPay(data, req.params.submissionId, t);
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  }
};
