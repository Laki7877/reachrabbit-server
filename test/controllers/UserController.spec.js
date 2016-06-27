'use strict';

var helpers = require('../common/helpers'),
    api     = helpers.api;

describe(apiPath, function() {
  before(helpers.before);
  after(helpers.after);

  describe('I have no Authorization Header', function() {
    it('should response with 401 Error', function(done) {
      api.get(apiPath)
      .expect(401, done);
    });
  });

  describe('I have an incorrect Authorization Header', function() {
    var incorrectToken = 'JWT eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ';

    it('should response with 403 Error', function(done) {
      api.get(apiPath)
        .set('Authorization', incorrectToken)
        .expect(403, done);
    });
  });

});
