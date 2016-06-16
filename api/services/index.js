/**
 * Expose all services on singular object
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';
var bulk        = require('bulk-require');

// export all services
module.exports = bulk(__dirname, ['**/*.js', '!index.js']);
