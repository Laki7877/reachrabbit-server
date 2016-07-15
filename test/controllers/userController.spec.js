'use strict';

var helpers = require('../common/helpers'),
    config  = require('config'),
    api     = helpers.api;

var profilePath = '/profiles';
var userBrandPath = '/users/brand';

/**
 * endpoint /me
 */
describe('GET ' + profilePath, function() {
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
      api.get(profilePath)
      .expect(401, done);
    });
  });

  describe('I have an incorrect token', function() {
    var incorrectToken = 'JWT eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ';

    it('should return 401', function(done) {
      api.get(profilePath)
        .set('Authorization', incorrectToken)
        .expect(401, done);
    });
  });

  describe('I have correct token', function(done) {
    it('should return 200', function(cb) {
      // get me
      api.get(profilePath)
        .set('Authorization', config.AUTHORIZATION_TYPE + ' ' + token)
        .expect(200, cb);
    });

    it('should return profile', function(cb) {
      // get me
      api.get(profilePath)
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

describe('POST ' + userBrandPath, function() {
  before(helpers.before);
  after(helpers.after);

  // update brand
  describe('Create new brand', function() {
    var brand = {
      email: 'new@gmail.com',
      password: '1234'
    };
    var token = '';

    it('should return with token', function(done) {
      api.post(userBrandPath)
        .send(brand)
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }
          expect(res.body.token).to.be.a('string');
          token = res.body.token;
          done();
        });
    });
    it('should update', function(done) {
      api.put(profilePath)
        .set('Authorization', config.AUTHORIZATION_TYPE + ' ' + token)
        .send({
        })
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }
          console.log(res.body);
            done();
        });
    });
  });

});
