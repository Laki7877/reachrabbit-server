/**
 * Handle global API routing
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var express   = require('express'),
    auth      = require('./middlewares/auth'),
    $         = require('./controllers');


module.exports = function() {
  // initiate express router object
  var router = express.Router({
    caseSensitive: true
  });

  /*********************************
   * Authentication
   *********************************/
  router.post('/auth/facebook', $.AuthController.facebook);

  /********************************
   * Register
   ********************************/
  router.post('/register/influencer', $.UserController.registerInfluencer);

  /*********************************
   * User
   *********************************/
  router.get('/me', auth(), $.UserController.profile);

  /*********************************
   * File Demo
   *********************************/

  router.get('/file_demo', $.FileDemoController.listAll);
  router.post('/file_demo', $.FileDemoController.upload);

  return router;
};
