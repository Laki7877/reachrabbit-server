/**
 * Campaign Application model
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.2
 */
'use strict';

module.exports = function(sequelize, DataTypes) {
  var CampaignSubmission = sequelize.define('CampaignSubmission', {
    id: {
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    }
  }, {
    classMethods: {
      associate: function(models) {
        CampaignSubmission.hasMany(models.CampaignRevision, {
          foreignKey: 'submissionId'
        });
      }
    }
  });
  return CampaignSubmission;
};
