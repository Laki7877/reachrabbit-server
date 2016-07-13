'use strict';

var helpers = require('../common/helpers'),
    authHelper = require('../../api/helpers/authHelper'),
    api     = helpers.api;

var loginPath = '/login';
var createBrandPath = '/users/brand';

describe('POST ' + createBrandPath, function() {
  before(helpers.before);
  after(helpers.after);

  describe('Create new brand', function() {
    var brand = {
      email: 'new@gmail.com',
      password: '1234'
    };
    it('should return 200', function(done) {
      api.post(createBrandPath)
        .send(brand)
        .expect(200, done);
    });
    it('should return with token', function(done) {
      api.post(createBrandPath)
        .send(brand)
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }
          expect(res.body.token).to.be.a('string');
          done();
        });
    });
  });
});
describe('POST ' + loginPath, function() {
  before(helpers.before);
  after(helpers.after);

  describe('Wrong email/password', function() {
    it('should return 400', function(done) {
      var user = {
        email: 'wrong@email.com',
        password: 'wrong'
      };
      api.post('/login')
        .send(user)
        .expect(400, done);
    });
  });

  describe('Correct email/password', function() {
    var user = {
        email: 'brand1@test.com',
        password: 'hackme'
    };

    it('should return 200', function(done) {
      api.post('/login')
        .send(user)
        .expect(200, done);
    });

    it('should return with correct token', function(done) {
      api.post('/login')
        .send(user)
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }

          // check returned token
          var data = res.body;
          expect(data).to.be.a('object');
          expect(data).to.have.property('token');

          // check validity of token
          authHelper.decode(data.token)
            .then(function(id) {
              done();
            }).catch(done);
        });
    });
  });
});
