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
    config    = require('confg'),
    sequelize = models.sequelize;

// create api base
var api = request(server);

module.exports = {
  /**
   * Reset database with fixture values
   *
   * @return     {Promise}  Q Promise
   */
  resyncDB: function() {
    return sequelize.sync({force: true, logging: false})
      .then(function() {
        return fixtures.loadFile('test/common/fixtures/**/*.json', models, {
          log: function(){}
        });
      });
  },
  /**
   * Drop all tables on database
   *
   * @return     {Promise}  Q Promise
   */
  cleanDB: function() {
    return sequelize.sync({force: true, logging: false});
  },

  brandLogin: function(done) {
    var self = this;
    var brand = {
      email: 'new@gmail.com',
      password: '1234'
    };
    api.post('/login')
      .send(brand)
      .end(function(err, res) {
        if(err) {
          return done(err);
        }
        self.brandToken = config.AUTHORIZATION_TYPE + ' ' + res.token;
        done();
      });
  },
  api: api
};