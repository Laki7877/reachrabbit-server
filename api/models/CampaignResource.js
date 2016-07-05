/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var CampaignResource = sequelize.define('CampaignResource', {
    campaignId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'Campaign',
        key: 'campaignId'
      }
    },
    resourceId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      references: {
        model: 'Resource',
        key: 'resourceId'
      }
    },
    isFeature: {
      type: DataTypes.BOOLEAN,
      allowNull: false,
      defaultValue: false
    }
  }, {
    tableName: 'CampaignResource',
    paranoid: true
  });

  return CampaignResource;
};
