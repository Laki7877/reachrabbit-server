/**
 * Redis server connector
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      (version_id)
 */
var Redis = require('ioredis');
var redis = {};

// create redis replica
function createLodis() {
  // lodis version
  var lodis = {};
  lodis._storage = {};

  // mimick redis fn
  lodis.hmset = function(key, value) {
    this._storage[key] = value;
  };
  lodis.hmget = function(key, cb) {
    cb(null, this._storage[key]);
  };
  return lodis;
}

module.exports = function(cb) {
  if(process.env.REDIS_URI) {
    // connect to redis server
    redis = new Redis(process.env.REDIS_URI, { lazyConnect: true });
    redis.connect().then(function() {
      //successfully connected
      cb(redis);
    }).catch(function() {
      //lodis mode
      redis = createLodis();
      cb(redis);
    });
  } else {
    //lodis mode
    redis = createLodis();
    cb(redis)
  }
};
