/**
 * Provide brand user service
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

var config  = require('config'),
    User 	= require('../models').User,
		Brand = require('../models').Brand,
    Resource = require('../models').Resource,
    cacheHelper = require('../helpers/cacheHelper'),
    authHelper = require('../helpers/authHelper');

var brandSchema = ['brandName'];

module.exports = {
  create: function(user, t) {
    if(user.profilePicture) {
      user.profilePicture = user.profilePicture.resourceId;
    }

		// create user with brand associated
  	return User.create(user, { include: [Brand], transaction: t }).then(function(createdUser) {
			return createdUser;
		});
  },
  update: function(user, t) {
    return User.findById(user.userId, {
      include: [Brand],
      transaction: t
    })
      .then(function(existingUser) {
        if(!existingUser) {
          return existingUser;
        }

        // update user
        _.extend(existingUser, user);

        return existingUser.save({ transaction: t });
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
  },
  findById: function(id) {
    return User.findById(id, {
      include: [{
        model: Brand,
        required: true
      }]
    })
    .then(function(user) {
      if(!user) {
        return user;
      }
      return Resource.findById(user.profilePicture).then(function(result) {
        user.profilePicture = result;

        if(result) {
          user.profilePicture.dataValues = process.env.S3_PUBLIC_URL + user.profilePicture.resourcePath;
        }
        return user;
      });
    })
    .then(function(user) {
      if(user) {
        _.extend(user.dataValues, user.Brand.dataValues);
      }
      return user;
    });
  },
  findByEmail: function(email) {
    return User.findOne({
      where: {
        email: email
      },
      include: [{
        model: Brand,
        required: true
      }]
    });
  }
};
