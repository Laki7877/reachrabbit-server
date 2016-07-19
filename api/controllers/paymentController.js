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
    paymentService = require('../services/paymentService'),
    sequelize = require('../models').sequelize,
    config = require('config');


module.exports = {
  listPayment: function(req, res, next) {
    Promise.try(function() {
      if(req.role !== config.ROLE.ADMIN) {
        return paymentService.listByUserId(req.user.userId, req.criteria);
      } else {
        return paymentService.list(req.criteria);
      }
    })
    .then(function(result) {
      return res.send(result);
    })
    .catch(next);
  },
  bulkUpdatePayment: function(req, res, next) {
    var data = req.body;
    sequelize.transaction(function(t) {
      return paymentService.updateAll(data, t);
    })
    .then(function(results) {
      return res.send(results);
    })
    .catch(next);
  }
};
