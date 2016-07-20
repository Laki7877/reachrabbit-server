/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var CampaignSubmissionResource = sequelize.define('campaignSubmissionProof', {
  }, {
    tableName: 'CampaignSubmissionProof'
  });

  return CampaignSubmissionResource;
};
