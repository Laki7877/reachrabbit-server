/**
 * Handle all authentication endpoints
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var Authom = require('authom'),
    moment = require('moment');

/**
 * Login with site username/password
 *
 * @param      {object}    req     The request
 * @param      {object}    res     The resource
 * @param      {Function}  next    The next
 */
function login(req, res, next) {
  async.waterfall(
  ], function(err, token) {
    if(err) return next(err);
    res.json({ token: token });
  });
}

/**
 * List of OAuth server handled
 */
Authom.createServer({
  service: 'facebook',
  id: process.env.FACEBOOK_APP_ID,
  secret: process.env.FACEBOOK_APP_SECRET
});

/**
 * Handle all OAuth service login event
 */
Authom.on('auth', function(req, res, data) {
  logger.debug('auth', data);
  // build db query object
  var query = {
    where: {},
    defaults: {}
  };

  // search or save service id to user database
  query.where[data.service] = data.id;
  query.defaults[data.service] = data.id;

  // query for user, then return jwt token
  async.waterfall([
      //find or create user by service id
      function(cb) {
        User.findOrCreate(query)
        .then(function(user, created) {
          return cb(null, user);
        }).catch(cb);
      },
      //encode jwt
      function(user, cb) {
        Auth.encodePayload(user.id, cb);
      }
    ], function(err, token) {
      if(err) {
        return res.status(httpStatus.INTERNAL_SERVER_ERROR).json(err);
      }
      res.json({token: token});
  });
});

/**
 * Handle all OAuth service error login event
 */
Authom.on('error', function(req, res, data) {
  logger.debug('err', data.error);
  res.status(httpStatus.INTERNAL_SERVER_ERROR).json(data.error);
});

/**
 * Pass oauth code to Authom server
 *
 * @param      {object}    req     The request
 * @param      {object}    res     The resource
 * @param      {Function}  next    The next
 */
function oauth(req, res, next) {
  req.params.service = req.swagger.params.service.value;
  req.params.code = req.swagger.params.code.value;
  Authom.app(req, res);
}

module.exports = {
  post: post,
  oauth: oauth
};
