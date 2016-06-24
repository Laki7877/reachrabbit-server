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

var multer = require('multer');
var upload = multer({ dest: '.tmp' });


module.exports = function() {
  // initiate express router object
  var router = express.Router({
    caseSensitive: true
  });

  /*********************************
   * Authentication
   *********************************/
  router.post('/auth/facebook', $.AuthController.facebook);
  router.post('/login', $.AuthController.login);

  /********************************
   * Register
   ********************************/
  router.post('/register/influencer', $.UserController.registerInfluencer);
  router.post('/register/brand', $.UserController.registerBrand);
  router.post('/confirm', $.UserController.confirmEmail);

  /*********************************
   * User
   *********************************/
  router.get('/me', auth(), $.UserController.profile);

  /*********************************
   * File Demo
   *********************************/

  router.get('/file_demo', $.FileDemoController.listAll);
  router.post('/file_demo', upload.single('slip'), $.FileDemoController.uploadSingle);
  router.get('/file_demo/:id', $.FileDemoController.getOne);

  return router;
};
