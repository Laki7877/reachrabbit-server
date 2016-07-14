/**
 * Handle user endpoints
 *
 * @author     Laki Sik <laki7877@gmail.com>
 * @since      0.0.1
 */
'use strict';


var User 	= require('../models').User,
    db      = require('../models'),
    brandService = require('../services/brandService');

module.exports = {
    testCreate : function (req, res, next) {
        var tmpUser = {
            name : "Laki Sik",
            email : "laki7877@gmail.com",
            password : "P@ssw0rd",
            brand :{
              brandName: 'test'
            }
        };

        db.sequelize.transaction(function(t) {
          var user = brandService.build();
          var brand = db.Brand.build(tmpUser.brand);
            return user.setBrand(brand, { transaction: t })
            .then(function() {
                return user.save({transaction: t});
            });
        })
        .then(function(result) {
            res.send(result.get({plain: true}));
        })
        .catch(next);
    },

    testDelete : function (req, res) {
        User.destroy({
            where : {
                email : 'laki7877@gmail.com'
            }
        }).then(function(users){
            res.send(users);
        });
    },

    testFind : function (req, res) {
        User.findAll({
            include: [{
                model: db.Resource,
                as: 'profilePicture'
            }, {
                model: db.Brand
            }]
        }).then(function(users){
            res.send(users);
        });
    },

    testUpdate : function (req, res) {
        User.update({
            deletedAt : null
        },{
            where : {
                email : 'laki7877@gmail.com'
            }
        }).then(function(users){
            res.send(users);
        });
    },
};
