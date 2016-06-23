/**
 * Provide facebook method
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
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

function getToken(data, done) {
  // params
  var params = {
    code: data.code,
    redirect_uri: data.redirectUri
  };
  // make request to facebook gettoken
  graph.authorize(_.extend(params, authConfig), function(err, token) {
    if(err) {
      return done(err);
    }
    return done(null, {
      token: token.access_token
    });
  });
}

/**
 * Gets the profile from facebook
 *
 * @param      {String}    token   access token
 * @param      {Function}  done    The done
 */
function getProfile(token, done) {
  var params = {
    access_token: token,
    fields: 'email,id,name,picture.width(300)'
  };
  graph.get('me', params, done);
}

module.exports = {
  getToken: getToken,
  getProfile, getProfile
};
