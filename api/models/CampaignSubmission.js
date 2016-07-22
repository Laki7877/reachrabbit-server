/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var CampaignSubmission = sequelize.define('campaignSubmission', {
    submissionId: {
      type: DataTypes.UUID,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    title: {
      type: DataTypes.STRING
    },
    status: {
      type: DataTypes.ENUM('wait for review', 'reject', 'need revision', 'wait for post', 'posted', 'paid'),
      defaultValue: 'wait for review',
      allowNull: false
    },
    proposalId: {
      type: DataTypes.UUID,
      allowNull: false,
      references: {
        model: 'CampaignProposal',
        key: 'proposalId'
      }
    },
    comment: {
      type: DataTypes.TEXT
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
        CampaignSubmission.belongsToMany(models.Resource, {
          through: models.CampaignSubmissionProof,
          foreignKey: 'submissionId',
          as: 'proof'
        });
        // submission belongs to one of the proposal
        CampaignSubmission.belongsTo(models.CampaignProposal, {
          foreignKey: 'proposalId'
        });
      }
    }
  });

  return CampaignSubmission;
};
