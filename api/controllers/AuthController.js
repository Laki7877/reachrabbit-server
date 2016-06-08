/**
 * Handle all authentication endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var passport = require('passport'),
  User = require('../models').User,
  Auth = require('../auth'),
  Authom = require('authom'),
  moment = require('moment');

/**
 * Login with site username/password
 *
 * @param      {object}    req     The request
 * @param      {object}    res     The resource
 * @param      {Function}  next    The next
 */
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
      //send back encoded jwt payload
      Auth.encodePayload(user.id, cb);
    }
  ], function(err, token) {
    if(err) return next(err);
    res.json({ token: token });
  });
}


/**
 * Handle all OAuth service login event
 */
Authom.on('auth', function(req, res, data) {
  logger.debug('auth', data);
  // build db query object
  var query = {
    where: {},
    defaults: {}
  };

  // search or save service id to user database
  query.where[data.service] = data.id;
  query.defaults[data.service] = data.id;

  // query for user, then return jwt token
  async.waterfall([
      //find or create user by service id
      function(cb) {
        User.findOrCreate(query)
        .then(function(user, created) {
          return cb(null, user);
        }).catch(cb);
      },
      //encode jwt
      function(user, cb) {
        Auth.encodePayload(user.id, cb);
      }
    ], function(err, token) {
      if(err) {
        return res.status(httpStatus.INTERNAL_SERVER_ERROR).json(err);
      }
      res.json({token: token});
  });
});

/**
 * Handle all OAuth service error login event
 */
Authom.on('error', function(req, res, data) {
  logger.debug('err', data.error);
  res.status(httpStatus.INTERNAL_SERVER_ERROR).json(data.error);
});

/**
 * Pass oauth code to Authom server
 *
 * @param      {object}    req     The request
 * @param      {object}    res     The resource
 * @param      {Function}  next    The next
 */
function oauth(req, res, next) {
  req.params.service = req.swagger.params.service.value;
  req.params.code = req.swagger.params.code.value;
  Authom.app(req, res);
}

module.exports = {
  post: post,
  oauth: oauth
};
