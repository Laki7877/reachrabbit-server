/**
 * Handle global API routing
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var express = require('express');
var ctrl    = require('./controllers');

module.exports = function() {
  // initiate express router object
  var router = express.Router({
    caseSensitive: true
  });

  /************************************
   * Route
   ************************************/
  router.get('/', ctrl.userController);

  return router;
};
