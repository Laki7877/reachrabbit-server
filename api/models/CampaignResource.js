/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var CampaignResource = sequelize.define('CampaignResource', {
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
