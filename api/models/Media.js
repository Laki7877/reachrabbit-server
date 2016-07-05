/* jshint indent: 2 */

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
          through: models.CampaignMedia,
          foreighKey: 'mediaId',
          otherKey: 'campaignId'
        });
      }
    }
  });

  return Media;
};
