/**
 * Handle search on query string
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

module.exports = function(field) {
  // default field is q
  field = field || 'q';

  return function(req, res, next) {
    if(_.isString(req.query[field])) {
      try {
        req.query[field] = JSON.parse(req.query[field]);
      } catch(e) {

      }
    }
    next();
  };
};
