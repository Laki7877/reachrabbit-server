/**
 * Provide cache functionality
 * Should wrap redis in the future
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var cache = {};

module.exports = {
  /**
   * Get content from cache by key
   *
   * @param      {String}  key     The key
   * @return     {Object|String|Number}  stored object
   */
  get: function(key) {
    return cache[key];
  },
  /**
   * Put content to cache by key
   *
   * @param      {String}  key     The key
   * @param      {Object|String|Number}  object  The object
   */
  put: function(key, object) {
    cache[key] = object;
  }
  /**
   * Remove content from cache at key
   *
   * @param      {String}  key     The key
   */
  remove: function(key) {
    _.unset(cache, key);
  }
};
