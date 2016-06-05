/**
 * User controller with CRUD
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var User = require('../models').User;

function get(req, res) {
    var id = req.swagger.params.id.value;
    res.json(User.findById(id));
}

module.exports = {
  get: get
};
