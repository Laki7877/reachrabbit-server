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

var ig = require('instagram-node').instagram();
var redirect_uri = process.env.INSTAGRAM_REDIRECT_URI;

ig.use({ client_id: process.env.INSTAGRAM_CLIENT_ID,
         client_secret: process.env.INSTAGRAM_CLIENT_SECRET });


module.exports = {

};
