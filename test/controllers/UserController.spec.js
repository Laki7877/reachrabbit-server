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
      email: 'lazada@gmail.com',
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

  describe('I have no Authorization Header', function() {
    it('should response with 401 Error', function(done) {
      api.get('/me')
      .expect(401, done);
    });
  });

  describe('I have an incorrect Authorization Header', function() {
    var incorrectToken = 'JWT eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ';

    it('should response with 401 Error', function(done) {
      api.get('/me')
        .set('Authorization', incorrectToken)
        .expect(401, done);
    });
  });

  describe('I have correct Authorization Header', function(done) {
    it('should return 200', function(cb) {
      // get me
      api.get('/me')
        .set('Authorization', config.AUTHORIZATION_TYPE + ' ' + token)
        .expect(200, cb);
    });

    it('should return user with email ' + user.email, function(cb) {
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
