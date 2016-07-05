/**
 * Provide user service
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var User        = require('../models').User,
    userCrud = require('./crudService')(User);

module.exports = {
  /**
   * Creates a user
   *
   * @param      {Object}    user    The user
   * @param      {Function}  done    The done
   */
  create: function(user) {
    return userCrud.create(user)
      .then(function(user) {
        return _.omit(user, ['password']);
      });
  }
};
