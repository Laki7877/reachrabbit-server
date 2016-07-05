/**
 * Provide facebook method
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @author     Pat Sabpisal <ecegrid@gmail.com>
 * @since      0.0.2
 */
'use strict';

var request = require('request'),
  graph = require('fbgraph');

var authConfig = {
  client_id: process.env.FACEBOOK_APP_ID,
  client_secret: process.env.FACEBOOK_APP_SECRET,
  version: '2.6'
};
var accessTokenUrl = 'https://graph.facebook.com/v2.6/oauth/access_token';

// set graph version
graph.setVersion(authConfig.version);

module.exports = {
  /**
   * Gets the short term access token.
   *
   * @param      {Object}    data    The data
   * @param      {Function}  done    The done
   */
  getToken: function (data, done) {
    // params
    var params = {
      code: data.code,
      redirect_uri: data.redirectUri
    };

    var authorize = Promise.promisify(graph.authorize, { context: graph });
    return authorize(_.extend(params, authConfig))
      .then(function(result) {
        return {
          token: result.access_token
        };
      });
  },
  /**
   * Gets fb profile
   *
   * @param      {String}    accessToken   The access token
   * @return     {Object} Promise
   */
  getProfile: function (accessToken) {
    var params = {
      access_token: accessToken,
      fields: 'email,id,name,picture.width(300)'
    };
    return new Promise(function(resolve, reject) {
      graph.get('me', params, function(err, profile) {
        if(err) {
          return reject(err);
        }
        return resolve(profile);
      });
    });
  },

  /**
   * Gets fb associated accounts
   *
   */
  getAssociatedAccounts: function (accessToken, userId) {
    var params = {
      access_token: accessToken,
      fields: 'fan_count,name,id,picture.type(large)'
    };
    return new Promise(function(resolve, reject) {
      graph.get(userId+ "/accounts", params, function(err, accounts) {
        if(err) {
          return reject(err);
        }
        return resolve(accounts);
      });
    });
  },


  getId: function (accessToken) {
    var params = {
      access_token: accessToken,
      fields: 'id'
    };
    return new Promise(function(resolve, reject) {
      graph.get('me', params, function(err, profile) {
        if(err) {
          return reject(err);
        }
        return resolve(profile.id);
      });
    });
  }

};
