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
    getActiveCategories: function (req, res, next) {
        dataService.findAllCategory(true);
          .then(function(category) {
            res.send(category);
          }).catch(next);
    },

    getActiveMedium: function (req, res, next) {
        dataService.findAllMedia(true)
          .then(function(media) {
            res.send(media);
          }).catch(next);
    },

    getBanks: function (req, res) {
        dataService.findAllBank();
          .then(function(bank) {
            res.send(bank);
          }).catch(next);
    }
};
