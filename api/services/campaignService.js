/**
 * Handle campaign resource
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

var User = require('../models').User,
	Campaign = require('../models').Campaign,
	criteriaHelper = require('../helpers/criteriaHelper');

module.exports = {
	findAllByOwner: function(brandId, criteria) {
		var opts = {
			where: { ownerId: brandId }
		};

		// extend with pagination criteria
		opts = _.extend(opts, criteria);

		return Campaign.findAndCountAll(opts);
	}
};
