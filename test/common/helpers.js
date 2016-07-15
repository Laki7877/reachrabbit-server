/**
 * Test helper
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var request   = require('supertest'),
    server    = require('../../app'),
    fixtures  = require('sequelize-fixtures'),
    models    = require('../../api/models'),
    config    = require('config'),
    sequelize = models.sequelize,
    expect    = require('chai').expect;

// create api base
var api = request(server);

var helpers = {};

helpers.checkPagination = function(data) {
  expect(data).to.be.a('object');
  expect(data.count).to.be.a('number');
  expect(data.rows).to.be.a('array');
  expect(data.rows[0]).to.be.a('object');
};

helpers.resyncDB = function() {
  return sequelize.sync({force: true, logging: false})
    .then(function() {
      return fixtures.loadFile('test/common/fixtures/**/*.json', models, {
        log: function() {}
      });
    })
    .then(function() {
      return fixtures.loadFile('test/common/fixtures/**/*.js', models, {
        log: function() {}
      });
    })
    .catch(function(e) {
      console.error(e);
    });
};

helpers.cleanDB = function() {
  return sequelize.sync({force: true, logging: false});
};

helpers.brandLogin = function(done) {
  var self = this;
  var brand = {
    email: 'brand1@test.com',
    password: 'hackme'
  };
  api.post('/login')
    .send(brand)
    .end(function(err, res) {
      if(err) {
        return done(err);
      }
      helpers.brandToken = config.AUTHORIZATION_TYPE + ' ' + res.body.token;
      done();
    });
};
helpers.influencerLogin = function(done) {
  var inf = {
    socialName: 'facebook',
    socialId: '128000600963755'
  };
  api.post('/tests/influencerLogin')
    .send(inf)
    .end(function(err, res) {
      if(err) {
        return done(err);
      }
      helpers.influencerToken = config.AUTHORIZATION_TYPE + ' ' + res.body.token;
      done();
    });
};

helpers.before = helpers.resyncDB;
helpers.after = helpers.cleanDB;
helpers.api = api;

module.exports = helpers;
