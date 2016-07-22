/**
 * Provide brand user service
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

var config  = require('config'),
    Promise = require('bluebird'),
    User 	= require('../models').User,
		Brand = require('../models').Brand,
		Bank = require('../models').Bank,
    Resource = require('../models').Resource,
    cacheHelper = require('../helpers/cacheHelper');

var include = [
  {
    model: Brand,
    required: true
  },
  {
    model: Resource,
    as: 'profilePicture'
  }
];

module.exports = {
  update: function(values, instance, t) {
    values.bankId = _.get(values, 'bank.bankId');
    values.profilePictureId = _.get(values, 'profilePicture.resourceId');
    values = _.omit(values, ['bank', 'profilePicture']);
    _.extend(instance, values);
    return instance.save({transaction: t});
  },
  create: function(values, t) {
    values.bankId = _.get(values, 'bank.bankId');
    values.profilePictureId = _.get(values, 'profilePicture.resourceId');
    values = _.omit(values, ['bank', 'profilePicture']);
    return User.create(_.extend({
      brand: {}
    }, values), {
      include: include,
      transaction: t
    });
  },
  list: function(criteria) {
    return User.findAndCountAll(_.extend({
      include: [{
        model: Brand,
        required: true
      }]
    }, criteria));
  },
  findByUserId: function(id) {
    return User.findById(id, {
      include: include
    });
  },
  findByEmail: function(email) {
    return User.findOne({
      where: {
        email: email
      },
      include: include
    });
  }
};