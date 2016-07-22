/**
 * Query filter helper for building sequelize query
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

module.exports = {
	createPaginatedQuery: function(page, size, order, direction) {
		var opts = {};
		if(page && size) {
			opts.offset = (page - 1) * size;
			opts.limit = size;
		}

		if(order && direction) {
			direction = _.upperCase(direction);
			if(!_.includes(['DESC', 'ASC'], direction)) {
				direction = 'ASC';
			}
			opts.order = [order, direction];
		}

		return opts;
	}
};
