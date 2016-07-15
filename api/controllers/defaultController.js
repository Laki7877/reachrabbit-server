/**
 * Handle user endpoints
 *
 * @author     Laki Sik <laki7877@gmail.com>
 * @since      0.0.1
 */
'use strict';
/* istanbul ignore next */

var User 	= require('../models').User,
    db      = require('../models'),
    influencerService = require('../services/influencerService');

module.exports = {
    testCreate : function (req, res, next) {
        var tmpUser = {
            name : "Laki Sik",
            email : "laki7877@gmail.com",
            password : "P@ssw0rd"
        };

        // i give up, just create -> process
        db.sequelize.transaction(function(t) {
          var user = db.User.build({}, {
            include: [{
              model: db.Influencer,
              include: [{
                model: db.Media,
                through: {
                  model: db.InfluencerMedia
                }
              }],
              required: true
            }, {
              model: db.Resource,
              as: 'profilePicture'
            }]
          });

          _.extend(user, tmpUser);

          var influencer = db.Influencer.build({
            about: 'me'
          });
          return db.Media.findOne({
            where: {
              mediaName: 'facebook'
            }
          }).then(function(media) {
            media.set('influencerMedia', {
              socialId: 'test'
            });
            influencer.set('media', media);
            user.set('influencer', influencer);
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
            }, {
              model: db.Influencer,
              include: [{
                model: db.Media,
                through: {
                  model: db.InfluencerMedia
                }
              }]
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
