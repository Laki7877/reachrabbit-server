/**
 * Redis server connector
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      (version_id)
 */
'use strict';
var Redis = require('ioredis');
var redis = {};

// create redis replica
function createLodis() {
  // lodis version
  var lodis = {};
  lodis._storage = {};

  // mimick redis fn
  lodis.hset = function(key, value) {
    this._storage[key] = value;
  };
  lodis.hget = function(key, cb) {
    cb(null, this._storage[key]);
  };
  lodis.hexists = function(key, cb) {
    cb(null, !_.isNil(this._storage[key]));
  };
  return lodis;
}

if(process.env.REDIS_URI) {
  // connect to redis server
  redis = new Redis(process.env.REDIS_URI, { lazyConnect: true });
} else {
  //lodis mode
  redis = createLodis();
}
module.exports = redis;
