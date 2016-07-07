/**
 * Handle all authentication endpoints
 *
 * @author     Laki Sik <laki7877@gmail.com>
 * @fixer      na
 * @since      0.0.1
 */
'use strict';

var dataService = require('../services/dataService');

module.exports = {
    getActiveCategories: function (req, res) {
        var promise = dataService.findAllCategory(true);
        promise.then(function(category) {
            res.send(category);
        });
    },

    getActiveMedium: function (req, res) {
        var promise = dataService.findAllMedia(true);
        promise.then(function(media) {
            res.send(media);
        });
    }
}
