/**
 * Handle all authentication endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @fixer      Pat Sabpisa <ssabpisa@me.com>
 * @since      0.0.1
 */
'use strict';

var moment      = require('moment'),
    authService = require('../services/authService'),
    facebookService = require('../services/facebookService'),
    googleService = require('../services/googleService'),
    userService = require('../services/userService'),
    userCrud    = require('../services/crudService')('User');

var googleApi = require('googleapis');

/*************************************************
 * OAuth Services
 *************************************************/
// list of auth services

function google(req,res,next){
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
 * Login with site username/password
 *
 * @param      {object}    req     The request
 * @param      {object}    res     The resource
 * @param      {Function}  next    The next
 */
function login(req, res, next) {
  var loginForm = _.pick(req.body, ['email', 'password']);

  // find user with email
  userCrud.findOne({ email: loginForm.email })
    .then(function(user) {
      if(!user) {
        throw new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid email/password');
      }
      return user.verifyPassword(loginForm.password)
        .then(function(eq) {
          if(!eq) {
            throw new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid email/password');
          }
          return user.getBrand();
        });
    })
    .then(function(influencer) {
      return authService.encode({});
    })
    .then(function(jwt) {
      return res.json({ token: jwt });
    })
    .catch(next);
}
// export module
module.exports = {
  login: login,
  facebook: facebook,
  google: google
};
