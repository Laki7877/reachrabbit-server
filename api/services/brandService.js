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
    Resource = require('../models').Resource,
    cacheHelper = require('../helpers/cacheHelper'),
    authHelper = require('../helpers/authHelper');

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
    _.extend(instance, values);
    return instance.save({transaction: t});
  },
  create: function(values, t) {
    return User.create(_.extend({
      brand: {}
    }, values), {
      include: include,
      transaction: t
    });
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
  },
  createToken: function(user, cache) {
    // cache user
    if(cache) {
      cacheHelper.set(user.userId, {
        user: user,
        role: config.ROLE.BRAND
      });
    }
    return authHelper.encode({
      userId: user.userId
    });
  }
};
