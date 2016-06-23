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
function createInfluencer(user, done) {
  var user = _.pick(user, ['email', 'contactNumber', 'facebook', 'name']);
  user.role = 'influencer';
  user.confirm = false;

  service.create(user, function(err, user) {
    if(err) {
      return done(err);
    }
    return done(_.omit(user, ['password']));
  });
}

service.createInfluencer = createInfluencer;

// export service
module.exports = service;
