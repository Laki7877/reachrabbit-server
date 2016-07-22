/**
 * Handle pagination on params
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

var criteriaHelper = require('../helpers/criteriaHelper');

module.exports = function(defaultsize, maxsize) {
  //
  if(!_.isInteger(defaultsize)) {
    defaultsize = 10
  }
  //
  if(!_.isInteger(maxsize)) {
    maxsize = 100
  }
  return function(req, res, next) {
    // no query
    if(!req.query) {
      return next();
    }

    // convert query to int
    req.query.page = _.isString(req.query.page) ? _.parseInt(req.query.page): 1;
    req.query.size = _.isString(req.query.size) ? _.parseInt(req.query.size) : defaultsize;

    if(req.query.size > maxsize) {
      req.query.size = maxsize;
    }

    var criteria = criteriaHelper.createPaginatedQuery(req.query.page, req.query.size, req.query.order, req.query.direction);

    // alias for sequelize
    req.criteria = _.extend({}, req.criteria, criteria);

    next();
  };
};
