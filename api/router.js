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
  router.post('/auth/facebook', $.authController.facebook);
  router.post('/login', $.authController.login);

  /********************************
   * Register
   ********************************/
  router.post('/register/influencer', $.userController.registerInfluencer);
  router.post('/register/brand', $.userController.registerBrand);
  router.post('/confirm', $.userController.confirmEmail);

  /*********************************
   * User
   *********************************/
  router.get('/me', auth(), $.userController.profile);

  /*********************************
   * File Demo
   *********************************/

  router.get('/file_demo', $.fileDemoController.listAll);
  router.post('/file_demo', upload.single('file'), $.fileDemoController.uploadSingle);
  router.get('/file_demo/:id', $.fileDemoController.getOne);

  return router;
};
