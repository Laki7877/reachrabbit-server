'use strict';

var helpers = require('../common/helpers'),
    config  = require('config'),
    api     = helpers.api;

var profilePath = '/profiles';
var userBrandPath = '/users/brand';
var userInfluencerPath = '/users/influencer';

// get profile
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

  describe('I have correct token', function() {
    it('should return 200', function(done) {
      // get me
      api.get(profilePath)
        .set('Authorization', config.AUTHORIZATION_TYPE + ' ' + token)
        .expect(200, done);
    });

    it('should return profile', function(done) {
      // get me
      api.get(profilePath)
        .set('Authorization', config.AUTHORIZATION_TYPE + ' ' + token)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }
          var data = res.body;
          expect(data.email).to.equal(user.email);
          expect(data).to.have.deep.property('profilePicture.resourceId', 'ed687098-7aeb-4b83-a931-1318d9141e2f');
          done();
        });
    });
  });
});

// update profile
describe('PUT ' + profilePath, function() {
  before(helpers.before);
  after(helpers.after);

  describe('With brand', function() {
    before(helpers.brandLogin);
    var brand = {
      email: 'new@gmail.com',
      password: 'newPassword'
    };

    it('should return updated user', function(done) {
      api.put(profilePath)
        .set('Authorization', helpers.brandToken)
        .send(brand)
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }
          var data = res.body;
          expect(data).to.have.property('email', 'new@gmail.com');
          done();
        });
    });
  });
  describe('With influencer', function() {
    before(helpers.influencerLogin);
    var influencer = {
      email: 'new@gmail.com',
      influencer: {
        web: 'test',
        socialAccounts: {
          'facebook' : {
            socialId: '1234'
          }
        }
      }
    };

    it('should return updated user', function(done) {
      api.put(profilePath)
        .set('Authorization', helpers.influencerToken)
        .send(influencer)
        .expect(200)
        .end(function(err, res) {
          if(err) {
            return done(err);
          }
          var data = res.body;
          expect(data).to.have.deep.property('influencer.influencerId', 'cb8cf696-ea59-4884-909e-0185ad36ca05');
          expect(data).to.have.deep.property('influencer.web', 'test');
          expect(data).to.have.deep.property('influencer.socialAccounts.facebook.socialId', '1234');
          done();
        });
    });
  });
});

// create brand
describe('POST ' + userBrandPath, function() {
  before(helpers.before);
  after(helpers.after);

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
  });
});

// get brand(s)
describe('GET ' + userBrandPath, function() {
  before(helpers.before);
  after(helpers.after);
  var id = 'df44da10-7a27-41f6-abe8-5908e8c4d56a';

  // get one brand
  it('should get paged brands', function(done) {
    api.get(userBrandPath)
      .expect(200)
      .end(function(err, res) {
        if(err) {
          return done(err);
        }
        var data = res.body;
        helpers.checkPagination(data);
        expect(data.rows[0]).to.have.property('brand');
        done();
      });
  });
  it('should get single brand', function(done) {
    api.get(userBrandPath + '/' + id)
      .expect(200)
      .end(function(err, res) {
        if(err) {
          return done(err);
        }
        var data = res.body;
        expect(data).to.have.property('userId', id);
        done();
      });
  });
});


// get Influencer(s)
describe('GET ' + userInfluencerPath, function() {
  before(helpers.before);
  after(helpers.after);
  var id = 'fd75cd48-f22c-49a1-9f73-2cfc246dcee3';

  // get one Influencer
  it('should get paged influencers', function(done) {
    api.get(userInfluencerPath)
      .expect(200)
      .end(function(err, res) {
        if(err) {
          return done(err);
        }
        var data = res.body;
        helpers.checkPagination(data);
        expect(data.rows[0]).to.have.property('influencer');
        done();
      });
  });
  it('should get single influencer', function(done) {
    api.get(userInfluencerPath + '/' + id)
      .expect(200)
      .end(function(err, res) {
        if(err) {
          return done(err);
        }
        var data = res.body;
        expect(data).to.have.property('userId', id);
        done();
      });
  });
});
