/**
 * Provide influencer user service
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

var User        = require('../models').User,
    Influencer  = require('../models').Influencer,
    InfluencerMedia  = require('../models').InfluencerMedia,
    Media  = require('../models').Media;

module.exports = {
  create: function(userObject, t) {
    var influencerSchema = ['about', 'bankAccount'];
    var mediaSchema = ['socialAccounts'];

    // split objects
    var newUser = _.omit(userObject, _.concat(influencerSchema, mediaSchema));
    var newInfluencer = _.pick(userObject, influencerSchema);
    var medias = userObject[mediaSchema];
    var mediaKeys = _.keys(medias);

    return User.create(_.extend({}, newUser, {
        Influencer: newInfluencer
      }), {
        include: [Influencer],
        transaction: t
      })
    .then(function(createdUser) {
      // get created user's influencer
      return createdUser.getInfluencer()
        .then(function(createdInfluencer) {
          // find medias by sent keys
          return Media.findAll({
            where: {
              mediaName: mediaKeys
            },
            transaction: t
          })
          .then(function(results) {
            // add through-model
            _.forEach(results, function(media) {
              media = _.extend(media, {
                through: {
                  model: InfluencerMedia,
                  socialId: medias[media.mediaName].id,
                  token: medias[media.mediaName].token
                }
              });
            });

            // execute query
            return createdInfluencer.addMedium(results, { transaction: t })
              .then(function() {
                // return user for final result
                return createdUser.get({plain: true});
              });
          });
        });
    });
  },
  findByMedia: function(providerName, socialId) {
    return User.findOne({
      include: [
        {
          //Influencer
          model: Influencer,
          include: [
            {
              // Media
              model: Media,
              where: { mediaName: providerName, isActive: true },
              required: true,
              through: {
                // InfluencerMedia
                model: InfluencerMedia,
                where: { socialId: socialId }
              }
            }
          ],
          required: true
        }
      ]
    });
  }
};
