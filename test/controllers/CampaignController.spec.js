'use strict';

var helpers = require('../common/helpers'),
    api     = helpers.api;

var path = '/campaigns';

describe('GET ' + path, function() {
	var brandToken, influencerToken;
	before(helpers.before);
	after(helpers.after);
	describe('Without Authorization', function() {
		it('should return 401', function(done) {
			api.get(path)
				.expect(401, done);
		});
	});
	describe('With Brand', function() {
		before(helpers.brandLogin);
		it('should return 200', function(done) {
			api.get(path)
				.set('Authorization', helpers.brandToken)
				.expect(200, done);
		});
		it('should return paginated object', function(done) {
			api.get(path)
				.set('Authorization', helpers.brandToken)
				.end(function(err, res) {
					if(err) {
						return done(err);
					}
					expect(res.body).to.be.a('object');
					expect(res.body.count).to.be.a('number');
					expect(res.body.row).to.be.a('array');
					done();
				});
		});
	});
});