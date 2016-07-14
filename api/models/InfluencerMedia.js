/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var InfluencerMedia = sequelize.define('influencerMedia', {
    influencerId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'influencer',
        key: 'influencerId'
      }
    },
    mediaId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'media',
        key: 'mediaId'
      }
    },
    socialId: {
      type: DataTypes.STRING
    },
    pageId: {
      type: DataTypes.STRING
    },
    token: {
      type: DataTypes.STRING
    }
  }, {
    tableName: 'InfluencerMedia'
  });

  return InfluencerMedia;
};
