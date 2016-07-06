/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var Media = sequelize.define('Media', {
    mediaId: {
      type: DataTypes.UUID,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    mediaName: {
      type: DataTypes.STRING
    },
    isActive: {
      type: DataTypes.BOOLEAN,
      defaultValue: false
    }
  }, {
    tableName: 'Media',
    paranoid: true,
    classMethods: {
      associate: function(models) {
        Media.belongsToMany(models.Campaign, {
          through: models.CampaignMedia
        });
        Media.belongsToMany(models.Influencer, {
          through: models.InfluencerMedia,
          foreignKey: 'mediaId'
        });
      }
    }
  });

  return Media;
};
