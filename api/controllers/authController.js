/**
 * Handle all authentication endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @fixer      Pat Sabpisa <ssabpisa@me.com>
 * @since      0.0.1
 */
'use strict';

var moment = require('moment'),
  config = require('config'),
  authService = require('../services/authService'),
  facebookService = require('../services/facebookService'),
  googleService = require('../services/googleService'),
  igService = require('../services/instagramService'),
  userService = require('../services/userService'),
  cacheHelper = require('../helpers/cacheHelper');

var googleApi = require('googleapis');
var ig = require('instagram-node').instagram();

/*************************************************
 * OAuth Services
 *************************************************/

/*
 *  google OAuth flow
 *
 *
 */
function google(req, res, next) {
  googleService.getToken(req.body.code)
    .then(function(oAuthCli) {
      var youtube = googleApi.youtube({
        version: 'v3',
        auth: oAuthCli
      });
      console.log(oAuthCli.credentials.access_token)
      var profileRq = youtube.channels.list({
        'part': 'snippet,statistics',
        'mine': true
      }, function(err, response) {
        console.log(response);
        var me = response.items[0];
        //TO POON : Just for "registration flow" ,
        //change provider to 'ahancer' or something

        if(me.statistics.subscriberCount < process.env.YOUTUBE_SUBSCRIBER_THRESHOLD){
          //TODO: May need to discuss error flow
          return res.status(403).send({
            "display": {
              "title": "From server",
              "message": "Sorry, " + me.snippet.title + ". You need at least " + process.env.YOUTUBE_SUBSCRIBER_THRESHOLD +
               " subscribers on YouTube to signup. GTFO. Just kidding.."
            },
            "exception_code": "AC83-01"
          });
        }

        res.send({
          'provider': 'google',
          'name': me.snippet.title,
          'id': me.id,
          'followers_count': me.statistics.subscriberCount,
          'picture': me.snippet.thumbnails.high.url,
          'token': oAuthCli.credentials.access_token
        });
      });


    })
    .catch(next);
}

/*
 *  instagram OAuth flow
 *
 *
 */
function instagram(req, res, next) {
  var token;
  igService.authorize_user(req.body.code)
    .then(function(result) {
      token = result.access_token;
      igService.applyToken(token);
      return igService.user(result.user.id);
    })
    .then(function(user){
      console.log(user);

      if(user.counts.followed_by < process.env.INSTAGRAM_FOLLOWER_THRESHOLD){
        //TODO: May need to discuss error flow
        return res.status(403).send({
          "display": {
            "title": "From server",
            "message": "Sorry, @" + user.username + ". You need at least " + process.env.INSTAGRAM_FOLLOWER_THRESHOLD +
             " followers on Instagram to signup. GTFO. Just kidding.."
          },
          "exception_code": "AC83-01"
        });
      }

      //TO POON : Just for "registration flow" ,
      //change provider to 'ahancer' or something
      return res.json({
        'provider': 'instagram',
        'name': user.full_name,
        'id': user.id,
        'followers_count': user.counts.followed_by,
        'picture': user.profile_picture,
        'token': token
      });
    })
    .catch(next);
}

/*
 *  facebook OAuth flow
 *
 *
 */

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
    },
    //get associated accounts
    function(profile){
      console.log('id', profile.id)
      return facebookService.getAssociatedAccounts(profile.token, "me")
      .then(function(accounts){
        if(!accounts) return _.extend({accounts: []},profile);
        return _.extend({
          accounts: accounts.data.map(function(ac){
            //confrom to same format as other endpoints
            ac.picture = ac.picture.data.url;
            return ac;
          })
        }, profile);
      });
    }
  ]).then(function(result) {
    //TO POON : Just for "registration flow" ,
    //change provider to 'ahancer' or something
    console.log("done fb login")
    console.log(result, 'fb');
    return res.json({
      'provider': 'facebook',
      'name': result.name,
      'accounts': result.accounts,
      'id': result.id,
      'email': result.email,
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
          if (!eq) {
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
      return res.send({
        token: token
      });
    })
    .catch(next);
}
// export module
module.exports = {
  brandLogin: brandLogin,
  facebook: facebook,
  google: google,
  instagram: instagram
};
