/**
 * Handle user endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var UserService = require('../services/UserService');

/**
 * Create new user
 *
 * @param      {Object}    req     The request
 * @param      {Object}    res     The resource
 * @param      {Function}  next    The next
 */
function create(req, res, next) {
  // filter user
  var user = _.pick(req.body, ['email', 'password']);

  // create new
  UserService.create(user, function(err, result) {
    if(err) return next(err);
    return res.json(result);
  });
}

/**
 * Find current user's information
 *
 * @param      {Object}    req     The request
 * @param      {Object}    res     The resource
 * @param      {Function}  next    The next
 */
function findMe(req, res, next) {
  if(req.user) {
    return res.json(req.user);
  } else {
    return next(new errors.NotFoundError('User not found'));
  }
}

module.exports = {
  create: create,
  findMe: findMe
};
