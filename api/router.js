/**
 * Handle global API routing
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @author     Pat Sabpisal <ecegrid@gmail.com>
 * @since      0.0.1
 */
'use strict';

var express   = require('express'),
    validate  = require('express-jsonschema').validate,
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
  router.post('/auth/google', $.authController.google);

  router.post('/login/brand', $.authController.brandLogin);

  /********************************
   * Register
   ********************************/
  router.post('/signup/influencer', $.userController.signupInfluencer);
  router.post('/signup/brand', $.userController.signupBrand);

  /*********************************
   * User
   *********************************/
  router.get('/me', auth(), $.userController.profile);

  /*********************************
   * File Demo
   *********************************/

  router.get('/file_demo', $.fileDemoController.listAll);
  router.post('/file_demo', upload.single('file'), $.fileDemoController.uploadSingle);

  return router;
};
