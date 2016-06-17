/**
 * Provide user service
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var User    = require('../models').User,
    service = require('./CrudService')(User); // inherit from crudService

// export service
module.exports = service;
