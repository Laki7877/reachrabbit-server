'use strict';

var helpers = require('../common/helpers'),
    config  = require('config'),
    api     = helpers.api;

/**
 * endpoint /me
 */
describe('GET /me', function() {
  var token = '';
  var user = {
      email: 'brand1@test.com',
      password: 'hackme'
  };
  beforeEach(function(done) {
    api.post('/login')
      .send(user)
      .end(function(err, res) {
        if(err) {
          return done(err);
        }
        token = res.body.token;
        done();
    });
  });
  before(helpers.before);
  after(helpers.after);

  describe('I have no token', function() {
    it('should return 401', function(done) {
      api.get('/me')
      .expect(401, done);
    });
  });

  describe('I have an incorrect token', function() {
    var incorrectToken = 'JWT eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ';

    it('should return 401', function(done) {
      api.get('/me')
        .set('Authorization', incorrectToken)
        .expect(401, done);
    });
  });

  describe('I have correct token', function(done) {
    it('should return 200', function(cb) {
      // get me
      api.get('/me')
        .set('Authorization', config.AUTHORIZATION_TYPE + ' ' + token)
        .expect(200, cb);
    });

    it('should return profile', function(cb) {
      // get me
      api.get('/me')
        .set('Authorization', config.AUTHORIZATION_TYPE + ' ' + token)
        .end(function(err, res) {
          if(err) {
            return cb(err);
          }
          expect(res.body.email).to.equal(user.email);
          cb();
        });
    });
  });
});
