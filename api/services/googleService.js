/**
 * Provide youtube method
 *
 * @author     Pat Sabpisal <ecegrid@gmail.com>
 * @since      0.0.1
 */
'use strict';

var request = require('request');
var google = require('googleapis');
var yt = google.youtube('v3');
var OAuth2 = google.auth.OAuth2;
var Promise = require('bluebird');

var oauth2Client = new OAuth2(process.env.GOOGLE_CLIENT_ID,
  process.env.GOOGLE_CLIENT_SECRET, process.env.GOOGLE_REDIRECT_URL);


module.exports = {
  getToken: function (code) {
    return new Promise(function(resolve, reject) {
      oauth2Client.getToken(code, function(err, tokens) {
        console.log("exchanging", code)
        if(!err) {
          console.log(tokens, 'is token');
          oauth2Client.setCredentials(tokens);
          resolve(oauth2Client);
        }else{
          console.log("rejected by google")
          reject(err)
        }
      });
    });
  }

};
