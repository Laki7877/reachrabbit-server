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
  router.post('/auth/instagram', $.authController.instagram);

  router.post('/login', $.authController.brandLogin);

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
   * Data
   *********************************/
  router.get('/data/categories', $.dataController.getActiveCategories);
  router.get('/data/medium', $.dataController.getActiveMedium);
  router.get('/data/banks', $.dataController.getBanks);
  /*********************************
   * File Demo
   *********************************/

  router.get('/file', $.fileController.listAll);
  router.post('/file', upload.single('file'), $.fileController.uploadSingle);
  router.post('/file/remote', $.fileController.fromRemote);

  return router;
};
