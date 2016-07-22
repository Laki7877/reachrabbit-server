/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var CampaignSubmissionResource = sequelize.define('campaignSubmissionResource', {
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
