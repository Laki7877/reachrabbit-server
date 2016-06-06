/**
 * Handle all authentication endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var User = require('../models').User,
  Auth = require('../auth');

function post(req, res, next) {
  async.waterfall([
    // find and check user password
    function(cb) {
      User.findOne({
        where: {
          username: req.body.username
        },
        attributes: ['id', 'username', 'password']
      }).then(function(user) {
        logger.debug(user);
        if(_.isNil(user)) {
          return cb(new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid username/password'));
        }
        user.verifyPassword(req.body.password, function(err, equal) {
          if(err) {
            cb(err);
          }
          logger.debug(err, equal);
          // not matching
          if(!equal) {
            return cb(new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid username/password'));
          }

          //match
          cb(null, user);
        })
      }, cb);
    },
    // serialize json web token
    function(user, cb) {
      Auth.encode(_.pick(user, ['id', 'username']), cb);
    }
  ], function(err, token) {
    if(err) return next(err);
    res.json(token);
  });
}

module.exports = {
  post: post
};
