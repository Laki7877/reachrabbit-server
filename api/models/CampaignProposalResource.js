/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var CampaignProposalResource = sequelize.define('CampaignProposalResource', {
    isFeature: {
      type: DataTypes.BOOLEAN,
      defaultValue: false
    }
  }, {
    tableName: 'CampaignProposalResource'
  });

  return CampaignProposalResource;
};
