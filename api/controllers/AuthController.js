/**
 * Handle all authentication endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @fixer      Pat Sabpisa <ssabpisa@me.com>
 * @since      0.0.1
 */
'use strict';

var Authom      = require('authom'),
    moment      = require('moment'),
    AuthService = require('../services/authService'),
    FacebookService = require('../services/FacebookService'),
    UserService = require('../services/UserService');

/*************************************************
 * OAuth Services
 *************************************************/
// list of auth services

function facebook(req, res, next) {
  async.waterfall([
    // get access token
    function(cb) {
      FacebookService.getToken(req.body, cb);
    },
    // get profile
    function(data, cb) {
      FacebookService.getProfile(data.token, function(err, profile) {
        if(err) {
          return next(err);
        }
        return cb(null, _.extend(profile, data));
      });
    },
    // try login with facebook
    function(data, cb) {
      AuthService.loginWithFB(data.id, function(err, token) {
        if(err) {
          return cb(null, data, false);
        }
        return cb(null, token, true);
      });
    }
  ], function(err, result, isLogin) {
    if(err) {
      return next(err);
    }
    // login mode
    if(isLogin) {
      return res.json({
        token: result,
        isLogin: isLogin
      })
    }
    // create new mode
    return res.json(_.extend(result, {
      isLogin: isLogin
    }));
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

  AuthService.login(loginForm.email, loginForm.password, function(err, token) {
    if(err) return next(err);
    return res.json({ token: token } );
  });
}

// export module
module.exports = {
  login: login,
  facebook: facebook
};
