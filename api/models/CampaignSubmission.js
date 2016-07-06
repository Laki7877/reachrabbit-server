/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var CampaignSubmission = sequelize.define('CampaignSubmission', {
    submissionId: {
      type: DataTypes.UUID,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    title: {
      type: DataTypes.STRING
    },
    description: {
      type: DataTypes.TEXT
    },
    createdBy: {
      type: DataTypes.STRING
    },
    updatedBy: {
      type: DataTypes.STRING
    }
  }, {
    tableName: 'CampaignSubmission',
    paranoid: true,
    classMethods: {
      associate: function(models) {
        CampaignSubmission.belongsTo(models.Campaign, {
          foreignKey: 'campaignId'
        });
        CampaignSubmission.belongsTo(models.Influencer, {
          foreignKey: 'influencerId'
        });
        CampaignSubmission.belongsToMany(models.Resource, {
          through: models.CampaignSubmissionResource,
          foreignKey: 'submissionId'
        });
      }
    }
  });

  return CampaignSubmission;
};
