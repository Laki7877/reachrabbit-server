/**
 * Provide influencer user service
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.3
 */
'use strict';

var config = require('config'),
    User        = require('../models').User,
    Influencer  = require('../models').Influencer,
    InfluencerMedia  = require('../models').InfluencerMedia,
    Media  = require('../models').Media,
    Resource = require('../models').Resource,
    authHelper = require('../helpers/authHelper'),
    cacheHelper = require('../helpers/cacheHelper');

var include = [{
  model: Influencer,
  include: [{
    model: Media,
    through: {
      model: InfluencerMedia
    }
  }],
  required: true
}, {
  model: Resource,
  as: 'profilePicture'
}];

// assign media to influencer
var assignMedias = function(values, instance, t) {
  var medias = values.socialAccounts;
  var mediaKeys = _.keys(medias);

  return Media.findAll({
    where: {
      mediaName: mediaKeys
    }
  })
  .then(function(medium) {
    // associate with media
    _.forEach(medium, function(media) {
      media = _.extend(media, {
        InfluencerMedia: {
          socialId: medias[media.mediaName].id,
          pageId: medias[media.mediaName].pageId,
          token: medias[media.mediaName].token
        }
      });
    });

    // associate with media
    instance.influencer.setMedia(medium, { transaction: t });
    return instance;
  });
};

module.exports = {
  update: function(values, instance, t) {
    return assignMedias(values, instance, t)
      .then(function(instance) {
        _.extend(instance, values);
        return instance.save({transaction: t});
      });
  },
  create: function(values, t) {
    return User.create(_.extend({
      influencer: {}
    }, values), {
      include: include,
      transaction: t
    })
    .then(function(instance) {
      return assignMedias(values, instance, t);
    });
  },
  list: function(criteria) {
    return User.findAndCountAll(_.extend({
      include: [{
        model: Influencer,
        required: true
      }]
    }, criteria));
  },
  findByUserId: function(id) {
    return User.findById(id, {
      include: include
    })
    .then(function(user) {
      if(user) {
        // media
        user.dataValues.socialAccounts = {};
        _.forEach(user.influencer.media, function(media) {
          user.dataValues.socialAccounts[media.mediaName] = {
            id: media.influencerMedia.socialId,
            pageId: media.influencerMedia.pageId
          };
        });
      }
      return user;
    });
  },
  findByMedia: function(providerName, socialId) {
    return User.findOne({
      include: [{
          //Influencer
          model: Influencer,
          include: [{
            model: Media,
            where: {
              mediaName: providerName,
              isActive: true
            },
            through: {
              where: {
                socialId: socialId
              }
            },
            required: true
          }],
          required: true
      }, {
        model: Resource,
        as: 'profilePicture'
      }]
    });
  },
  createToken: function(user, cache) {
    // cache user
    if(cache) {
      cacheHelper.set(user.userId, {
        user: user,
        role: config.ROLE.INFLUENCER
      });
    }
    // encode userid
    return authHelper.encode({
      userId: user.userId
    });
  },
  login: function(providerName, socialId) {
    return this.findByMedia(providerName, socialId)
      .then(function(user) {
        if(!user) {
          throw new Error('no user found');
        }
      });
  }
};
