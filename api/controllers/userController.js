/**
 * Handle user endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var userService = require('../services/userService'),
    crudService = require('../services/crudService')('User'),
    mailService = require('../services/mailService'),
    authService = require('../services/authService'),
    facebookService = require('../services/facebookService'),
    config = require('config');

/**
 * Create new influencer
 *
 * @param      {Object}    req     The request
 * @param      {Object}    res     The resource
 * @param      {Function}  next    The next
 */
function registerInfluencer(req, res, next) {
  if(!req.body.token) {
    return next(new errors.NotFoundError('Facebook Access Token'));
  }
  // create new user
  async.waterfall([
    // get facebook id by token
    facebookService.getId(req.body.token),
    function(id) {
      return crudService.findOne({

      })
    },
    // generate hash for email
    function(user) {
      return authService.encode(user.id)
        .then(function(hash) {
          return [user, hash];
        })
    },
    // send mail
    function(result) {
      var user = result[0];
      var hash = result[1];
      return mailService.send(user.email, 'ProjectX - Confirm ME', 'email_confirmation', {
        confirmUrl: config.EMAIL_CONFIRMATION_INFLUENCER_URL + '?q=' + hash
      }).then(function() {
        return user;
      });
    }
  ]).then(function(result) {
    return res.json(result);
  }).catch(next);
}
/**
 * Create new brand
 *
 * @param      {Object}    req     The request
 * @param      {Object}    res     The resource
 * @param      {Function}  next    The next
 */
function registerBrand(req, res, next) {
  // create new user
  async.waterfall([
    // create unconfirmed brand
    userService.createBrand(req.body, cb),
    // generate hash for email
    function(user) {
      return authService.encode(user.id)
        .then(function(hash) {
          return [user, hash];
        });
    },
    // send mail
    function(result) {
      var user = result[0];
      var hash = result[1];
      return mailService.send(user.email, 'ProjectX - Confirm ME', 'email_confirmation', {
        confirmUrl: config.EMAIL_CONFIRMATION_INFLUENCER_URL + '?q=' + hash
      }).then(function() {
        return user;
      });
    }
  ]).then(function(result) {
    return res.json(result);
  }).catch(next);
}
/**
 * Confirm user account with email
 *
 * @param      {Object}    req     The request
 * @param      {Object}    res     The resource
 * @param      {Function}  next    The next
 */
function confirmEmail(req, res, next) {
  // email token
  var token = req.body.token;
  if(!token) {
    return next(new errors.NotFoundError('Email token'));
  }
  async.waterfall([
    authService.decode(token),
    function(id) {
      return userService.update(id, {
        confirm: true
      });
    },
    function(user) {
      return authService.encode(user.id);
    }
  ]).then(function(result) {
      return res.json({
        token: result
      });
  }).catch(next);
}
/**
 * Find current user's information
 *
 * @param      {Object}    req     The request
 * @param      {Object}    res     The resource
 * @param      {Function}  next    The next
 */
function profile(req, res, next) {
  if(req.user) {
    return res.json(req.user);
  } else {
    return next(new errors.NotFoundError('User not found'));
  }
}

module.exports = {
  registerInfluencer: registerInfluencer,
  registerBrand: registerBrand,
  confirmEmail: confirmEmail,
  profile: profile
};
