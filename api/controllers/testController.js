/**
 * Provide test functions
 *
 * FOR TESTMODE ONLY
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';
/* istanbul ignore next */

var influencerService = require('../services/influencerService');

module.exports = {
  /**
   * Bypass social media authentication and get influencer token
   *
   * @param      {Object}    req     The request
   * @param      {Object}    res     The resource
   * @param      {Function}  next    The next
   */
  influencerBypassLogin: function(req, res, next) {
    return influencerService.findByMedia(req.body.socialName, req.body.socialId)
      .then(function(user) {
        if(user) {
          return influencerService.createToken(user, true);
        }
        throw new errors.HttpStatusError(httpStatus.NOT_FOUND);
      })
      .then(function(result) {
        res.send({
          token: result
        });
      }).catch(next)
  }
};
