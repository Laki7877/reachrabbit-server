/**
 * Provide brand user service
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

var User 	= require('../models').User,
		Brand = require('../models').Brand;

module.exports = {
  create: function(brand, transaction) {
		var user = _.omit(brand, ['brandName']);
		var brand = _.pick(brand, ['brandName']);

		// create user with brand associated
  	return User.create(_.extend({}, user, {
			Brand: brand
		}), { include: [Brand], transaction: transaction }).then(function(user) {
			return user.get({plain: true});
		});
  },
  findByEmail: function(email) {
    return User.findOne({
      where: {
        email: email
      },
      include: [Brand]
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
