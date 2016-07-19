'use strict';

var moment = require('moment'),
    helpers = require('../common/helpers'),
    api     = helpers.api;

var path = '/campaigns';
var myPath = '/mycampaigns';

describe('GET ' + myPath, function() {
  before(helpers.before);
  after(helpers.after);
  var id = '865b7f55-0316-47b0-9704-bc24eaba1dc5';

  // brand
  describe('With Brand', function() {
    before(helpers.brandLogin);
    it('should return campaign pages', function(done) {
      api.get(myPath)
        .set('Authorization', helpers.brandToken)
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }
          var data = res.body;
          helpers.checkPagination(data);

          expect(data.rows[0]).to.have.property('campaignId');
          expect(data.rows[0]).to.have.property('brandId', '86d9ebb5-78e2-4c8c-8eb6-f0e61010e2d6');
          done();
        });
    });
  });

  // influencer
  describe('With Influencer', function() {
    before(helpers.influencerLogin);
    it('should return campaign pages', function(done) {
      api.get(myPath)
        .set('Authorization', helpers.influencerToken)
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }
          var data = res.body;
          helpers.checkPagination(data);
          expect(data.rows[0]).to.have.property('campaignId');
          expect(data.rows[0]).to.have.property('brandId', '86d9ebb5-78e2-4c8c-8eb6-f0e61010e2d6');
          done();
        });
    });
  });
});

describe('GET ' + path, function() {
	before(helpers.before);
	after(helpers.after);
  var id = '865b7f55-0316-47b0-9704-bc24eaba1dc5';

  // brand
	describe('With Brand', function() {
		before(helpers.brandLogin);
		it('should return campaign pages', function(done) {
			api.get(path)
				.set('Authorization', helpers.brandToken)
        .expect(200)
				.end(function(err, res) {
					if(err) {
						return done(err);
					}
          var data = res.body;
          helpers.checkPagination(data)

          expect(data.rows[0]).to.have.property('campaignId');
          expect(data.rows[0]).to.have.property('brandId', '86d9ebb5-78e2-4c8c-8eb6-f0e61010e2d6');
					done();
				});
		});
    it('should return single campaign', function(done) {
      api.get(path + '/' + id)
        .set('Authorization', helpers.brandToken)
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }
          var data = res.body;
          expect(data).to.have.property('campaignId', '865b7f55-0316-47b0-9704-bc24eaba1dc5');
          done();
        })
    });
	});

  // influencer
  describe('With Influencer', function() {
    before(helpers.influencerLogin);
    it('should return campaign pages', function(done) {
      api.get(path)
        .set('Authorization', helpers.influencerToken)
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }
          var data = res.body;
          helpers.checkPagination(data);
          expect(data.rows[0]).to.have.property('campaignId');
          expect(data.rows[0]).to.have.property('brandId', '86d9ebb5-78e2-4c8c-8eb6-f0e61010e2d6');
          done();
        });
    });
    it('should return single campaign', function(done) {
      api.get(path + '/' + id)
        .set('Authorization', helpers.influencerToken)
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }
          var data = res.body;
          expect(data).to.have.property('campaignId', '865b7f55-0316-47b0-9704-bc24eaba1dc5');
          done();
        })
    });
  });
});

describe('POST ' + path, function() {
  before(helpers.before);
  after(helpers.after);

  var date = new Date();

  var campaign = {
    title: 'New campaign',
    description: 'some description',
    proposalDeadline: date,
    submissionDeadline: date,
    createdBy: 'someone',
    updatedBy: 'someone',
    category: {
      categoryName: 'Travel'
    },
    resource: [{
      resourceId: 'ed687098-7aeb-4b83-a931-1318d9141e2f'
    }],
    media: [{
      mediaId: 'google'
    }]
  };

  // with brand
  describe('With brand', function() {
    before(helpers.brandLogin);
    it('should create new campaign', function(done) {
      api.post(path)
        .set('Authorization', helpers.brandToken)
        .send(campaign)
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }
          var data = res.body;
          console.log(data);
          expect(data).to.have.property('title', campaign.title);
          expect(data).to.have.property('description', campaign.description);
          expect(data).to.have.property('proposalDeadline', moment(date).toISOString());
          expect(data).to.have.property('submissionDeadline', moment(date).toISOString());
          expect(data).to.have.property('createdBy', campaign.createdBy);
          expect(data).to.have.property('updatedBy', campaign.updatedBy);
          expect(data).to.have.property('brandId', '86d9ebb5-78e2-4c8c-8eb6-f0e61010e2d6');
          expect(data).to.have.property('resource').that.is.a('array');
          expect(data).to.have.property('media').that.is.a('array');
          done();
        });
    });
  });

  // with influencer
  describe('With influencer', function() {
    before(helpers.influencerLogin);
    it('should not create campaign', function(done) {
      api.post(path)
        .set('Authorization', helpers.influencerToken)
        .send(campaign)
        .expect(401, done);
    });
  });

  // with guest
  describe('With guest', function() {
    it('should not create campaign', function(done) {
      api.post(path)
        .send(campaign)
        .expect(401, done);
    });
  });
});

describe('PUT ' + path, function() {
  before(helpers.before);
  after(helpers.after);
});
