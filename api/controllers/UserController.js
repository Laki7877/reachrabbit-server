/**
 * User controller with CRUD
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var User = require('../models').User;

/**
 * Get user by id
 *
 * @param      {object}  req     The request
 * @param      {object}  res     The resource
 */
function get(req, res, next) {
  var id = req.swagger.params.id.value;
  User.findOne({
    where: { id: id },
    attributes: ['username', 'facebook']
  }).then(function(user) {
    if(_.isNil(user)) {
      return next(new errors.NotFoundError('User with id ' + id));
    }
    return res.json(user);
  })
  .catch(next);
}

/**
 * Create new user
 *
 * @param      {object}  req     The request
 * @param      {object}  res     The resource
 */
function post(req, res, next) {
  async.waterfall([function(cb) {
    // check if username exist
    User.findOne({
      where: { username: req.body.username },
      attributes: ['username']
    })
    .then(function(user) {
      if(!_.isNil(user)) {
        return cb(new errors.AlreadyInUseError(user.username, 'username'));
      }
      cb(null);
    })
    .catch(cb);
  }, function(cb) {
    // create the user
    User.create(_.pick(req.body, ['username', 'password']))
      .then(function(user) {
        cb(null, user.get({plain: true}));
      }, cb);
  }], function(err, user) {
    // resolver
    if(err) return next(err);
    return res.json(_.omit(user, ['password']));
  });
}

module.exports = {
  get: get,
  post: post
};
