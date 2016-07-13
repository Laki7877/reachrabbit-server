/**
 * Handle campaign
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @fixer      Pat Sabpisa <ssabpisa@me.com>
 * @since      0.0.3
 */
'use strict';

var config = require('config'), 
	campaignService = require('../services/campaignService');

module.exports = {
	list: function(req, res, next) {
		if(req.role === config.ROLE.BRAND) {
			return campaignService.list()
				.then(function(campaigns) {
					res.send(campaigns);
				})
				.catch(next);
		}
	}
};