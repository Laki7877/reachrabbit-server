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
    sequelize = models.sequelize;

// create api base
var api = request(server);

/**
 * Reset database with fixture values
 *
 * @return     {Promise}  Q Promise
 */
function resyncDB() {
  return sequelize.sync({force: true, logging: false})
    .then(function() {
      return fixtures.loadFile('test/common/fixtures/**/*.json', models, {
        log: function(){}
      });
    });
}
/**
 * Drop all tables on database
 *
 * @return     {Promise}  Q Promise
 */
function cleanDB() {
  return sequelize.drop({logging: false, cascade: true});
}
// module export
module.exports = {
  api: api,
  before: resyncDB,
  after: cleanDB
};
