'use strict';

var helpers = require('../common/helpers'),
    authHelper = require('../../api/helpers/authHelper'),
    api     = helpers.api;

var loginPath = '/login';

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
    it('should return 400',function(done){
      var user = {
        email: 'brand1@test.com',
        password: 'wrong'
      };
      api.post('/login')
        .send(user)
        .expect(400,done);
    });
    it('should return 400',function(done){
      var user = {
        email: 'test2@test.com',
        password: 'test1234'
      };
      api.post('/login')
        .send(user)
        .expect(400,done);
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