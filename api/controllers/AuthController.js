/**
 * Handle all authentication endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var Authom      = require('authom'),
    moment      = require('moment'),
    AuthService = require('../services/AuthService'),
    FacebookService = require('../services/FacebookService'),
    UserService = require('../services/UserService');

/*************************************************
 * OAuth Services
 *************************************************/
// list of auth services

function facebook(req, res, next) {
  async.waterfall([
    function(cb) {
      FacebookService.getToken(req.body, cb);
    },
    function(data, cb) {
      FacebookService.getProfile(data.token, function(err, profile) {
        if(err) {
          return next(err);
        }
        return cb(null, _.extend(profile, data));
      });
    }
  ], function(err, result) {
    if(err) {
      return next(err);
    }
    return res.json(result);
  });
}

/**************************************************
 * API Login Service
 *************************************************/
/**
 * Login with site username/password
 *
 * @param      {object}    req     The request
 * @param      {object}    res     The resource
 * @param      {Function}  next    The next
 */
function login(req, res, next) {
  var loginForm = _.pick(req.body, ['email', 'password']);

  AuthService.login(req.body.email, req.body.password, function(err, token) {
    if(err) return next(err);
    return res.json({ token: token } );
  });
}

// export module
module.exports = {
  login: login,
  facebook: facebook
};
