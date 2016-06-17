'use strict';

var helpers = require('../common/helpers'),
    api     = helpers.api;

var apiPath = '/me';

describe(apiPath, function() {
  before(helpers.before);
  after(helpers.after);

  describe('I have no Authorization Header', function() {
    it('should response with 401 Error', function(done) {
      api.get(apiPath)
      .expect(401, done);
    });
  });

});
