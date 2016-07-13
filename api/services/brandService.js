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
    Resource = require('../models').Resource;

module.exports = {
  create: function(userObject, t) {
    
    if(userObject.profilePicture) {
      userObject.profilePicture = userObject.profilePicture.resourceId;
    }
		// create user with brand associated
  	return User.create(userObject, { include: [Brand], transaction: t }).then(function(createdUser) {
			return createdUser.get({plain: true});
		});
  },
  createToken: function(user, cache) {
    // cache user
    if(cache) {
      cacheHelper.set(user.userId, _.extend(user, {role: config.ROLE.BRAND}));
    }
    return this.encode({
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
      })
    })
    .then(function(user) {
      if(user) {
        _.extend(user.dataValues, user.Brand.dataValues);
        _.unset(user, ['Brand']);
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
