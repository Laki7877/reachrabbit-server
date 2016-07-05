/**
 * Provide cache functionality
 * Should wrap redis in the future
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var Redis = require('ioredis');
var redis = new Redis(process.env.REDIS_URI, {
  lazyConnect: true
});
var lodis = {};

module.exports = {
  /**
   * Get content from cache by key
   *
   * @param      {String}  key     The key
   * @return     {Object|String|Number}  stored object
   */
  get: function(key) {
    return redis.get(key)
      .then(function(value) {
        try {
          return CJSON.parse(value);
        }
        catch(e) {
          return value;
        }
      })
      .catch(function(err) {
        console.error(err);
        return lodis[key];
      });
  },
  /**
   * Put content to cache by key
   *
   * @param      {String}  key     The key
   * @param      {Object|String|Number}  object  The object
   */
  set: function(key, object) {
    var value = null;
    try {
      value = CJSON.stringify(object);
    }
    catch(e) {
      value = object;
    }
    redis.set(key, object)
      .catch(function() {
        lodis[key] = object;
      });
  },
  /**
   * Remove content from cache at key
   *
   * @param      {String}  key     The key
   */
  remove: function(key) {
    redis.del(key)
      .catch(function() {
        _.unset(lodis, [key]);
      });
  }
};
