/**
 * Handle user endpoints
 *
 * @author     Laki Sik <laki7877@gmail.com>
 * @since      0.0.1
 */
'use strict';


var User 	= require('../models').User;

module.exports = {
    testCreate : function (req, res) {
        var tmpUser = {
            name : "Laki Sik",
            email : "laki7877@gmail.com",
            password : "P@ssw0rd"
        };
        User.create(tmpUser).then(function(user) {
            res.send(user);
        });
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
            attributes : [
                'email'
            ]
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
