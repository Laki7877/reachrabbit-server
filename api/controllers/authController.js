/**
 * Handle all authentication endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var Authom      = require('authom'),
    moment      = require('moment'),
    authService = require('../services/authService'),
    facebookService = require('../services/facebookService'),
    userService = require('../services/userService');

/*************************************************
 * OAuth Services
 *************************************************/
// list of auth services

function facebook(req, res, next) {
  async.waterfall([
    // get access token
    function(cb) {
      facebookService.getToken(req.body, cb);
    },
    // get profile
    function(data, cb) {
      facebookService.getProfile(data.token, function(err, profile) {
        if(err) {
          return next(err);
        }
        return cb(null, _.extend(profile, data));
      });
    },
    // try login with facebook
    function(data, cb) {
      authService.loginWithFB(data.id, function(err, token) {
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

  authService.login(loginForm.email, loginForm.password, function(err, token) {
    if(err) return next(err);
    return res.json({ token: token } );
  });
}

// export module
module.exports = {
  login: login,
  facebook: facebook
};
