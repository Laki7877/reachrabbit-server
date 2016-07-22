'use strict';

var helpers = require('../common/helpers'),
    api     = helpers.api;

describe('GET /data/*', function() {
  before(helpers.before);
  after(helpers.after);

  // categories
  describe('GET /data/categories', function() {
    it('should return categories', function(done) {
      api.get('/data/categories')
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }

          var data = res.body;
          expect(data).to.be.a('array');
          expect(data).to.have.deep.property('[0].categoryId')
            .that.is.a('string');
          expect(data).to.have.deep.property('[0].categoryName')
            .that.is.a('string');
          done();
        });
    })
  });

  // social medias
  describe('GET /data/medium', function() {
    it('should return social medias', function(done) {
      api.get('/data/medium')
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }
          var data = res.body;

          // bbd
          expect(data).to.be.a('array');
          expect(data).to.have.deep.property('[0].mediaId')
            .that.is.a('string');
          expect(data).to.have.deep.property('[0].mediaName')
            .that.is.a('string');

          done();
        });
    })
  });

  // banks
  describe('GET /data/banks', function() {
    it('should return banks', function(done) {
      api.get('/data/banks')
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }

          var data = res.body;
          expect(data).to.be.a('array');
          expect(data).to.have.deep.property('[0].bankId')
            .that.is.a('string');
          expect(data).to.have.deep.property('[0].bankName')
            .that.is.a('string');

          done();
        });
    })
  });

});
