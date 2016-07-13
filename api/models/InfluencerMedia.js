/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var InfluencerMedia = sequelize.define('InfluencerMedia', {
    influencerId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'Influencer',
        key: 'influencerId'
      }
    },
    mediaId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'Media',
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
