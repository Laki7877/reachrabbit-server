/**
 * Handle all authentication endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var User = require('../models').User,
  Auth = require('../auth'),
  moment = require('moment');

function post(req, res, next) {
  async.waterfall([
    // find and check user password
    function(cb) {
      //find user with username
      User.findOne({
        where: {
          username: req.body.username
        },
        attributes: ['id', 'username', 'password']
      }).then(function(user) {

        //user not found
        if(_.isNil(user)) {
          return cb(new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid username/password'));
        }

        //verify user pass
        user.verifyPassword(req.body.password, function(err, equal) {
          if(err) return cb(err);
          // password not match
          if(!equal) {
            return cb(new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid username/password'));
          }
          //match
          cb(null, user);
        });
      }, cb);
    },
    // serialize json web token
    function(user, cb) {
      //jwt payload
      var payload = {
        sub: user.id,
        iat: moment().unix(),
        exp: moment().add(process.env.JWT_EXPIRATION_TIME || 24, 'hours').unix()
      };
      Auth.encode(_.pick(user, ['id', 'username']), cb);
    }
  ], function(err, token) {
    if(err) return next(err);
    res.json({
      token: token
    });
  });
}

module.exports = {
  post: post
};
