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
  Promise = require('bluebird'),
  authService = require('../services/authService'),
  facebookService = require('../services/facebookService'),
  googleService = require('../services/googleService'),
  igService = require('../services/instagramService'),
  userService = require('../services/userService'),
  brandService = require('../services/brandService'),
  influencerService = require('../services/influencerService'),
  cacheHelper = require('../helpers/cacheHelper');

var googleApi = require('googleapis');
var ig = require('instagram-node').instagram();

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
      return user.verifyPassword(password)
        .then(function(eq) {
          if (!eq) {
            throw new errors.HttpStatusError(httpStatus.BAD_REQUEST, 'Invalid email/password');
          }
          return user;
        });
    })
    // encode user
    .then(function(user) {
      // cache user and return token
      return authService.createTokenForBrand(user, true);
    })
    // send
    .then(function(token) {
      return res.send({ token: token });
    })
    .catch(next);
}

/*************************************************
 * OAuth Services
 *************************************************/

/*
 *  google OAuth flow
 *
 */
function google(req, res, next) {
  googleService.getToken(req.body.code)
    .then(function(oAuthCli) {
      var youtube = googleApi.youtube({
        version: 'v3',
        auth: oAuthCli
      });

      // convert to promise
      var list = Promise.promisify(youtube.channels.list, { context: youtube.channels });

      // youtube listing
      return list({
        'part': 'snippet,statistics',
        'mine': true
      })
      .then(function(response) {
        var me = response.items[0];

        // find influencer in media table
        return influencerService.findByMedia('youtube', me.id)
          .then(function(user) {
            if(!user) {
              // not found
              if(me.statistics.subscriberCount < process.env.YOUTUBE_SUBSCRIBER_THRESHOLD){
                //TODO: May need to discuss error flow
                return res.status(403).send({
                  'display': {
                    'title': 'From server',
                    'message': 'Sorry, ' + me.snippet.title + '. You need at least ' + process.env.YOUTUBE_SUBSCRIBER_THRESHOLD +
                     ' subscribers on YouTube to signup. GTFO. Just kidding..'
                  },
                  'exception_code': 'AC83-01'
                });
              }
              return res.send({
                'provider': 'google',
                'name': me.snippet.title,
                'id': me.id,
                'followers_count': me.statistics.subscriberCount,
                'picture': me.snippet.thumbnails.high,
                'token': oAuthCli.credentials.access_token
              });
            } else {
              // found, create token and return
              authService.createTokenForInfluencer(user, true)
                .then(function(token) {
                  res.send({
                    isLogin: true,
                    token: token
                  });
                });
            }
          });
      });
    }).catch(next);
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
    .then(function(ig) {
      return influencerService.findByMedia('instagram', ig.id)
        .then(function(user) {
          if(!user) {
            // signup flow
            if(ig.counts.followed_by < process.env.INSTAGRAM_FOLLOWER_THRESHOLD){
                //TODO: May need to discuss error flow
                return res.status(403).send({
                  'display': {
                    'title': 'From server',
                    'message': 'Sorry, @' + ig.username + '. You need at least ' + process.env.INSTAGRAM_FOLLOWER_THRESHOLD +
                     ' followers on Instagram to signup. GTFO. Just kidding..'
                  },
                  'exception_code': 'AC83-01'
                });
              }

              // get profile
              return res.send({
                'provider': 'instagram',
                'name': ig.full_name,
                'id': ig.id,
                'followers_count': ig.counts.followed_by,
                'picture': {
                  url: ig.profile_picture
                },
                'token': token
              });
          } else {
            // login flow
            authService.createTokenForInfluencer(user, true)
              .then(function(token) {
                res.send({
                  isLogin: true,
                  token: token
                });
              });
          }
        });
    }).catch(next);
}

/*
 *  facebook OAuth flow
 *
 *
 */

function facebook(req, res, next) {
  facebookService.getToken(req.body)
    .then(function(data) {
      return facebookService.getProfile(data.token)
        .then(function(profile) {
          return _.extend(profile, data);
        });
    })
    .then(function(profile) {
      return influencerService.findByMedia('facebook', profile.id)
        .then(function(user) {
          if(!user) {
            // signup flow
            return facebookService.getAssociatedAccounts(profile.token, 'me')
              .then(function(accounts){
                if(!accounts) {
                  return _.extend({accounts: []}, profile);
                }
                return _.extend({
                  accounts: accounts.data.map(function(ac){
                    //confrom to same format as other endpoints
                    ac.picture = ac.picture.data;
                    return ac;
                  })
                }, profile);
              })
              .then(function(result) {
                // signup mode
                return res.send({
                  'provider': 'facebook',
                  'name': result.name,
                  'accounts': result.accounts,
                  'id': result.id,
                  'email': result.email,
                  'picture': result.picture.data,
                  'token': result.token
                });
            });
          } else {
            // login flow
            return authService.createTokenForInfluencer(user, true)
              .then(function(token) {
                return res.send({
                  token: token,
                  isLogin: true
                });
            });
          }
        });
    }).catch(next);
}
// export module
module.exports = {
  brandLogin: brandLogin,
  facebook: facebook,
  google: google,
  instagram: instagram
};
