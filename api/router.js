/**
 * Handle global API routing
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var express = require('express'),
    auth    = require('./middlewares/auth'),
    $       = require('./controllers');

module.exports = function() {
  // initiate express router object
  var router = express.Router({
    caseSensitive: true
  });

  /*********************************
   * Authentication
   *********************************/
  router.post('/auth', $.AuthController.login);
  router.post('/auth/:service', $.AuthController.oauth);
  router.get( '/auth/:service', $.AuthController.oauth);

  /*********************************
   * User
   *********************************/
  router.get('/me', auth(), $.UserController.findMe);
  router.post('/users', $.UserController.create);

  return router;
};
