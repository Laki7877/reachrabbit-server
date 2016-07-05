/**
 * Handle all authentication endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @fixer      Pat Sabpisa <ssabpisa@me.com>
 * @since      0.0.1
 */
'use strict';

var moment          = require('moment'),
    config          = require('config'),
    authService     = require('../services/authService'),
    facebookService = require('../services/facebookService'),
    googleService   = require('../services/googleService'),
    userService     = require('../services/userService'),
    cacheHelper     = require('../helpers/cacheHelper');

var googleApi = require('googleapis');

/*************************************************
 * OAuth Services
 *************************************************/
// list of auth services

function google(req, res, next){
  googleService.getToken(req.body.code)
  .then(function(oAuthCli){
    var youtube = googleApi.youtube({ version: 'v3', auth: oAuthCli});
    console.log(oAuthCli.credentials.access_token)
    var profileRq = youtube.channels.list({
      'part': 'snippet',
      'mine': true
    }, function(err, response){
        var me = response.items[0];
        res.send({
          'name': me.snippet.title,
          'id': me.id,
          'picture': me.snippet.thumbnails.high.url,
          'token': oAuthCli.credentials.access_token
        });
    });


  })
  .catch(next);
}

function facebook(req, res, next) {
  async.waterfall([
    // get access token
    function() {
      return facebookService.getToken(req.body);
    },
    // get profile
    function(data) {
      return facebookService.getProfile(data.token)
        .then(function(profile) {
          return _.extend(profile, data);
        });
    }
  ]).then(function(result) {
    console.log('facebook', result);

    return res.json({
      'name': result.name,
      'id': result.id,
      'picture': result.picture.data.url,
      'token': result.token
    });
  }).catch(next);
}

/**************************************************
 * API Login Service
 *************************************************/
/**
 * Brand login with casual email/password
 *
 * @param      {object}    req     The request
 * @param      {object}    res     The resource
 * @param      {Function}  next    The next
 */
function brandLogin(req, res, next) {
  var email = req.body.email;
  var password = req.body.password;

  // find brand by email
  brandService.findByEmail(email)
    // verify password
    .then(function(user) {
      return user.verifyPassword()
        .then(function(eq) {
          if(!eq) {
            throw new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid email/password');
          }
          return user;
        });
    })
    // encode user
    .then(function(user) {
      // cache user
      cacheHelper.set(user.userId, {
        user: user,
        role: config.ROLE.BRAND
      });
      return authService.encode({
        userId: user.userId
      });
    })
    // send
    .then(function(token) {
      return res.json({ token: token });
    })
    .catch(next);
}
// export module
module.exports = {
  brandLogin: brandLogin,
  facebook: facebook,
  google: google
};
