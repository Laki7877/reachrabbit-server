/**
 * Wraps around instagram node driver
 *
 * @author     Pat Sabpisal <ecegrid@gmail.com>
 * @since      0.0.1
 */
'use strict';

var ig = require('instagram-node').instagram();
var redirect_uri = process.env.INSTAGRAM_REDIRECT_URI;

ig.use({ client_id: process.env.INSTAGRAM_CLIENT_ID,
         client_secret: process.env.INSTAGRAM_CLIENT_SECRET });


module.exports = {
  /**
   * Gets access token via explicit flow oauth.
   *
   * @param      {Object}    data    The data
   * @param      {Function}  done    The done
   */
  getToken: function (data, done) {

  }

};
