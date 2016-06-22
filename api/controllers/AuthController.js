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
    UserService = require('../services/UserService');

/*************************************************
 * OAuth Services
 *************************************************/
// list of auth servers
var authServer = {
  facebook: {
    id: process.env.FACEBOOK_APP_ID,
    secret: process.env.FACEBOOK_APP_SECRET,
    fields: ['email', 'name']
  }
};
// list of auth middleware
var authHandler = {
  facebook: function(req, res, data, done) {
    done(data.data);
  }
};
// initiate oauth servers
_.forOwn(authServer, function(server, service) {
  Authom.createServer(_.extend({ service: service }, server));
});
// on oauth success
Authom.on('auth', function(req, res, data) {
  if(_.has(authHandler, data.service)) {
    // call middleware
    authHandler[data.service](req, res, data, function(err, result) {
      if(err) res.status(500).json({message: err});
      res.json(result);
    });
  } else {
    // just return the data
    res.json(data.data);
  }
});
// on oauth error
Authom.on('error', function(req, res, data) {
  res.status(httpStatus.INTERNAL_SERVER_ERROR).json(data.error);
});

function oauth(req, res, next) {
  Authom.app(req, res, next);
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
  oauth: oauth
};
