/**
 * Campaign Application model
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.2
 */
'use strict';

module.exports = function(sequelize, DataTypes) {
  var CampaignRevision = sequelize.define('CampaignRevision', {
    id: {
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    }
  }, {
    classMethods: {
      associate: function(models) {
        CampaignRevision.belongsTo(models.CampaignSubmission, {
          foreignKey: 'submissionId'
        });
      }
    }
  });
  return CampaignRevision;
};
