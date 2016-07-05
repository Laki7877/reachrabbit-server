/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var CampaignSubmissionResource = sequelize.define('CampaignSubmissionResource', {
    isFeature: {
      type: DataTypes.BOOLEAN,
      allowNull: false,
      defaultValue: false
    }
  }, {
    tableName: 'CampaignSubmissionResource'
  });

  return CampaignSubmissionResource;
};
