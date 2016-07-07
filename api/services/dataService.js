/**
 * Provide brand user service
 *
 * @author     Laki Sik <laki7877@gmail.com>
 * @since      0.0.1
 */
'use strict';

var Category 	= require('../models').Category;
var Media 	= require('../models').Media;

module.exports = {
    findAllCategory: function(activeFlag) {
        activeFlag = activeFlag || true;
        return Category.findAll({
            where: {
                isActive: activeFlag
            },
            attributes: [
                'categoryId',
                'categoryName'
            ]
        });
    },

    findAllMedia: function(activeFlag){
        activeFlag = activeFlag || true;
        return Media.findAll({
            where: {
                isActive: activeFlag
            },
            attributes: [
                'mediaId',
                'mediaName'
            ]
        });
    }
};