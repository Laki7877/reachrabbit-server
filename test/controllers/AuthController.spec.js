'use strict';

var helpers = require('../common/helpers'),
    authService = require('../../api/services/authService'),
    api     = helpers.api;

var path = '/login';

describe('POST /login', function() {
  var brand = require('../common/fixtures/brand.json');
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
        email: 'lazada@gmail.com',
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
          var data = res.body;
          assert.typeOf(data, 'object');
          assert.property(data, 'token');
          assert.typeOf(data.token, 'string');
          authService.decode(data.token)
            .then(function(id) {
              done();
            }).catch(done);
        });
    });
  });
});
