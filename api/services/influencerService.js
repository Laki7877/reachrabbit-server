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
    Bank  = require('../models').Bank,
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
}, {
  model: Bank
}];

var processSocialAccounts = function(user) {
  user.influencer.dataValues.socialAccounts = {};
  _.forEach(user.influencer.media, function(media) {
    user.influencer.dataValues.socialAccounts[media.mediaId] = {
      mediaId: media.mediaId,
      socialId: media.influencerMedia.socialId,
      pageId: media.influencerMedia.pageId,
      token: media.influencerMedia.token
    };
  });
  return user;
}
// assign media to influencer
var assignMedias = function(values, instance, t) {
  if(!values) {
    return Promise.resolve(instance);
  }
  var medias = values;
  var mediaKeys = _.keys(medias);
  var promises = [];

  _.forOwn(medias, function(media, key) {
    // upsert each m2m model
    _.extend(media, {
      mediaId: key,
      influencerId: instance.influencer.influencerId
    });
    promises.push(InfluencerMedia.upsert(media, {transaction: t}));
  });

  // should finish all before sending back
  return Promise.all(promises)
    .then(function() {
      return instance;
    });
};

module.exports = {
  populateSocialAccounts: function(user) {
    return processSocialAccounts(user);
  },
  update: function(values, instance, t) {
    var media = _.get(values, 'influencer.socialAccounts');
    var influencer = _.get(values, 'influencer');

    values.bankId = _.get(values, 'bank.bankId');
    values.profilePictureId = _.get(values, 'profilePicture.resourceId');

    values = _.omit(values, ['bank', 'profilePicture', 'influencer']);

    return instance.update(values, {transaction: t})
      .then(function() {
        return instance.influencer.update(influencer, {transaction: t});
      })
      .then(function() {
        return assignMedias(media, instance, t);
      });
  },
  create: function(values, t) {
    var media = _.get(values, 'influencer.socialAccounts');
    values.bankId = _.get(values, 'bank.bankId');
    values.profilePictureId = _.get(values, 'profilePicture.resourceId');
    values = _.omit(values, ['bank', 'profilePicture']);

    return User.create(_.extend({
      influencer: {}
    }, values), {
      include: include,
      transaction: t
    })
    .then(function(instance) {
      return assignMedias(media, instance, t);
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
        processSocialAccounts(user);
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
              mediaId: providerName,
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
