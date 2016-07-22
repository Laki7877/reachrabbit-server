/**
 * Wraps around instagram node driver
 * Provide seamless pathway to get instagram data
 * NOTE: this is not for getting data on user's behalf
 * but uses our own authorization token
 *
 * @author     Pat Sabpisal <ecegrid@gmail.com>
 * @since      0.0.1
 */
'use strict';

var ig = require('instagram-node').instagram(),
  redirect_uri = process.env.INSTAGRAM_REDIRECT_URI,
  Promise = require('bluebird');

module.exports = {
  applyToken: function(token){
    ig.use({ client_id: process.env.INSTAGRAM_CLIENT_ID,
          access_token: token,
         client_secret: process.env.INSTAGRAM_CLIENT_SECRET });
  },
  authorize_user : function(code){
    ig.use({ client_id: process.env.INSTAGRAM_CLIENT_ID,
         client_secret: process.env.INSTAGRAM_CLIENT_SECRET });

    return new Promise(function(resolve, reject) {
      ig.authorize_user(code, redirect_uri, function(err, result){
          if(err) return reject(err);
          resolve(result);
      });
    });
  },
  user: function(uid){
    return new Promise(function(resolve, reject) {
      ig.user(uid, function(err, result){
          if(err) return reject(err);
          resolve(result);
      });
    });
  }
};
