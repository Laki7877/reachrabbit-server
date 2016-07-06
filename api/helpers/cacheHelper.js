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
  lazyConnect: true,
  retryStrategy: function(times) {
    return false;
  }
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
    return new Promise(function(resolve, reject) {
      redis.get(key)
        .then(function(value) {
          try {
            // try parse as object
            var parsed = CJSON.parse(value);
            resolve(parsed);
          }
          catch(e) {
            // not object value
            resolve(value);
          }
        })
        .catch(function(err) {
          // use lodis instead
          if(_.has(lodis, key)) {
            resolve(lodis[key]);
          } else {
            reject(err);
          }
        });
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
