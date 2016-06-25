/**
 * Handle user endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var userService = require('../services/userService'),
    mailService = require('../services/mailService'),
    authService = require('../services/authService'),
    config = require('config');

/**
 * Create new influencer
 *
 * @param      {Object}    req     The request
 * @param      {Object}    res     The resource
 * @param      {Function}  next    The next
 */
function registerInfluencer(req, res, next) {
  // create new user
  async.waterfall([
    // create unconfirmed inf
    function(cb) {
      userService.createInfluencer(req.body, cb);
    },
    // generate Jwt hash
    function(user, cb) {
      authService.encode(user.email, function(err, hash) {
        if(err) {
          return cb(err);
        }
        return cb(null, user, hash);
      });
    },
    // send mail
    function(user, hash, cb) {
      mailService.send(user.email, 'ProjectX - Confirm ME', 'email_confirmation', {
        confirmUrl: config.EMAIL_CONFIRMATION_INF_URL + '?q=' + hash
      }).then(function() {
        cb(null, user);
      }).catch(cb);
    }
  ], function(err, result) {
    if(err) {
      return next(err);
    }
    return res.json(result);
  });
}

function registerBrand(req, res, next) {
  // create new user
  async.waterfall([
    // create unconfirmed brand
    function(cb) {
      userService.createBrand(req.body, cb);
    },
    // generate Jwt hash
    function(user, cb) {
      authService.encode(user.id, function(err, hash) {
        if(err) {
          return cb(err);
        }
        return cb(null, user, hash);
      });
    },
    // send mail
    function(user, hash, cb) {
      mailService.send(user.email, 'ProjectX - Confirm ME', 'email_confirmation', {
        confirmUrl: config.EMAIL_CONFIRMATION_BRAND_URL + '?q=' + hash
      }).then(function() {
        cb(null, user);
      }).catch(cb);
    }
  ], function(err, result) {
    if(err) {
      return next(err);
    }
    return res.json(result);
  });
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

  async.waterfall([
    function(cb) {
      authService.decode(token, cb);
    },
    function(id, cb) {
      userService.update(id, {
        confirm: true
      }, cb);
    },
    function(user, cb) {
      authService.encode(user.id, cb);
    }
  ], function(err, result) {
      if(err) {
        return next(err);
      }
      return res.json({
        token: result
      });
  });
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
