/**
 * Handle pagination on params
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

module.exports = function(defaultCount, maxCount) {
  if(!_.isInteger(defaultCount) || !_.isInteger(maxCount)) {
    throw new Error('pagination middleware args should be number');
  }

  return function(req, res, next) {
    // no query
    if(!req.query) {
      return next();
    }
    req.query.page = _.isString(req.query.page) ? _.parseInt(req.query.page) || 1 : 1;
    req.query.count = _.isString(req.query.count) ? _.parseInt(req.query.count) : maxCount;

    if(req.query.count > maxCount) {
      req.query.count = maxCount;
    }

    next();
  };
};
