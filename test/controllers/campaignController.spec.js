'use strict';

var helpers = require('../common/helpers'),
    api     = helpers.api;

var path = '/campaigns';

describe('GET ' + path, function() {
	before(helpers.before);
	after(helpers.after);
  // brand
	describe('With Brand', function() {
		before(helpers.brandLogin);
		it('should return 200', function(done) {
			api.get(path)
				.set('Authorization', helpers.brandToken)
				.expect(200, done);
		});
		it('should return campaign pages', function(done) {
			api.get(path)
				.set('Authorization', helpers.brandToken)
				.end(function(err, res) {
					if(err) {
						return done(err);
					}
					expect(res.body).to.be.a('object');
					expect(res.body.count).to.be.a('number');
					expect(res.body.rows).to.be.a('array');
          expect(res.body.rows[0]).to.be.a('object');

          expect(res.body.rows[0]).to.have.property('campaignId');
          expect(res.body.rows[0]).to.have.property('brandId', '86d9ebb5-78e2-4c8c-8eb6-f0e61010e2d6');
					done();
				});
		});
	});

  // influencer
  describe('With Influencer', function() {
    before(helpers.influencerLogin);

    it('should return 200', function(done) {
      api.get(path)
        .set('Authorization', helpers.influencerToken)
        .expect(200, done);
    });
    it('should return campaign pages', function(done) {
      api.get(path)
        .set('Authorization', helpers.influencerToken)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }
          expect(res.body).to.be.a('object');

          expect(res.body.count).to.be.a('number');
          expect(res.body.count).to.equal(2); // length = 2

          expect(res.body.rows).to.be.a('array');
          expect(res.body.rows[0]).to.be.a('object');

          expect(res.body.rows[0]).to.have.property('campaignId');
          expect(res.body.rows[0]).to.have.property('brandId', '86d9ebb5-78e2-4c8c-8eb6-f0e61010e2d6');
          done();
        });
    });
  });

});
