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
   * Creates an influencer.
   *
   * @param      {Object}    user    The user
   * @param      {Function}  done    The done
   */
  createInfluencer: function(user) {
    user = _.pick(user, ['email', 'contactNumber', 'facebook', 'name', 'password']);
    user.role = 'influencer';
    user.confirm = false;

    // create influencer
    return userCrud.create(user)
      .then(function(user) {
        // omit password
        return _.omit(user, ['password']);
      });
  },
  /**
   * Creates an brand.
   *
   * @param      {Object}    user    The user
   * @param      {Function}  done    The done
   */
  createBrand: function(user) {
    user = _.pick(user, ['email', 'brandName', 'contactNumber', 'name', 'password']);
    user.role = 'brand';
    user.confirm = false;

    return userCrud.create(user)
      .then(function(user) {
        // omit password
        return _.omit(user, ['password']);
      });
  }
};
