/**
 * Initialize test variables
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

require('dotenv-extended').load();
process.env.NODE_ENV = 'test';

global.chai   = require('chai');
global.should = chai.should();
global.expect = chai.expect;
global.assert = chai.assert;
global._      = require('lodash');
