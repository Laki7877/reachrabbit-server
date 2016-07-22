/**
 * Handle schemas
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';
var bulk        = require('bulk-require');

// export all controllers
module.exports = bulk(__dirname, ['**/*.js', '!index.js']);