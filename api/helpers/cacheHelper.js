/**
 * Provide cache functionality
 * Should wrap redis in the future
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var cache = {};

/**
 * Get content from cache by key
 *
 * @param      {String}  key     The key
 * @return     {Object|String|Number}  stored object
 */
function get(key) {
  return cache[key];
}

/**
 * Put content to cache by key
 *
 * @param      {String}  key     The key
 * @param      {Object|String|Number}  object  The object
 */
function put(key, object) {
  cache[key] = object;
}

/**
 * Remove content from cache at key
 *
 * @param      {String}  key     The key
 */
function remove(key) {
  _.unset(cache, key);
}

module.exports = {
  get: get,
  put: put,
  remove: remove
};
