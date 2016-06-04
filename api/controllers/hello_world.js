'use strict';
var util = require('util');

/**
 * hello world
 *
 * @param      {object}  req     The request
 * @param      {object}  res     The resource
 */
function hello(req, res) {
  // variables defined in the Swagger document can be referenced using req.swagger.params.{parameter_name}
  var name = req.swagger.params.name.value || 'stranger';
  var hello = util.format('Hello, %s!', name);

  // this sends back a JSON response which is a single string
  res.json(hello);
}

module.exports = {
  hello: hello
};