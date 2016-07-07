/**
 * Provide brand user service
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

var User 	= require('../models').User,
		Brand = require('../models').Brand,
    Resource = require('../models').Resource;

module.exports = {
  create: function(userObject, t) {
    var brandSchema = ['brandName'];
    var newUser = _.omit(userObject, brandSchema);
    var newBrand = _.pick(userObject, brandSchema);

		// create user with brand associated
  	return User.create(_.extend({}, newUser, {
			Brand: newBrand
		}), { include: [Brand], transaction: t }).then(function(createdUser) {
			return createdUser.get({plain: true});
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
        return user;
      })
    })
    .then(function(user) {
      _.extend(user.dataValues, user.Brand.dataValues);
      _.unset(user, ['Brand']);
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
    })
    .then(function(user) {
      if(!user) {
        throw new errors.NotFoundError('User');
      }
      if(!user.Brand) {
        throw new errors.NotFoundError('User');
      }
      return user;
    });
  }
};
