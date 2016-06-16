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
  res.json({});
}

var oauthServer = {
  facebook: {
    id: process.env.FACEBOOK_APP_ID,
    secret: process.env.FACEBOOK_APP_SECRET,
    fields: ['email', 'name']
  }
}
var oauthHandler = {
  facebook: function(req, res, data, done) {
    done(data.data);
  }
};

/**
 * List of OAuth server handled
 */
_.forOwn(oauthServer, function(server, service) {
  Authom.createServer(_.extend({ service: service }, server));
});

/**
 * Handle all OAuth service login event
 */
Authom.on('auth', function(req, res, data) {
  if(_.has(oauthHandler, data.service)) {
    oauthHandler[data.service](req, res, data, function(err, result) {
      if(err) res.status(500).json({message: err});
      res.json(result);
    });
  } else {
    res.json(data.data);
  }
});

/**
 * Handle all OAuth service error login event
 */
Authom.on('error', function(req, res, data) {
  logger.debug('err', data.error);
  res.status(httpStatus.INTERNAL_SERVER_ERROR).json(data.error);
});

module.exports = {
  oauth: Authom.app
};
