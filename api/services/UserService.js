/**
 * Provide user service
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var User    = require('../models').User,
    service = require('./CrudService')(User); // inherit from crudService

/**
 * Creates an influencer.
 *
 * @param      {Object}    user    The user
 * @param      {Function}  done    The done
 */
service.createInfluencer = function(user, done) {
  var user = _.pick(user, ['email', 'contactNumber', 'facebook', 'name']);
  user.role = 'influencer';
  user.confirm = false;

  service.create(user, function(err, user) {
    if(err) {
      return done(err);
    }
    return done(null, _.omit(user, ['password']));
  });
};

/**
 * Creates an brand.
 *
 * @param      {Object}    user    The user
 * @param      {Function}  done    The done
 */
service.createBrand = function(user, done) {
  var user = _.pick(user, ['email', 'brandName', 'contactNumber', 'facebook', 'name', 'password']);
  user.role = 'brand';
  user.confirm = false;

  service.create(user, function(err, user) {
    if(err) {
      return done(err);
    }
    return done(null, _.omit(user, ['password']));
  });
};

// export service
module.exports = service;
