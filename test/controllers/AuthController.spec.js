'use strict';

var helpers = require('../common/helpers'),
    authService = require('../../api/services/authService'),
    api     = helpers.api;

var path = '/login';

describe('POST ' + path, function() {
  before(helpers.before);
  after(helpers.after);

  describe('Wrong email/password', function() {
    it('should return 400', function(done) {
      var user = {
        email: 'wrong@email.com',
        password: 'wrong'
      };
      api.post(path)
        .send(user)
        .expect(400, done);
    });
  });

  describe('Correct email/password', function() {
    var user = {
        email: 'tester@gmail.com',
        password: 'test1234'
    };

    it('should return 200', function(done) {
      api.post(path)
        .send(user)
        .expect(200, done);
    });

    it('should return with correct token', function(done) {
      api.post(path)
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
