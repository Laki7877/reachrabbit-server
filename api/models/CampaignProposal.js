/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var CampaignProposal = sequelize.define('campaignProposal', {
    proposalId: {
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
    proposePrice: {
      type: DataTypes.DECIMAL(12,2)
    },
    status: {
      type: DataTypes.ENUM('propose','reject','needrevision', 'accept'),
      defaultValue: 'propose',
      allowNull: false
    },
    createdBy: {
      type: DataTypes.STRING
    },
    updatedBy: {
      type: DataTypes.STRING
    },
  }, {
    tableName: 'CampaignProposal',
    paranoid: true,
    classMethods: {
      associate: function(models) {
        CampaignProposal.belongsTo(models.Campaign, {
          foreignKey: 'campaignId'
        });
        CampaignProposal.belongsTo(models.Influencer, {
          foreignKey: 'influencerId'
        });

        // proposal belongs to one of submission, if passed
        CampaignProposal.belongsTo(models.CampaignSubmission, {
          foreignKey: 'submissionId'
        });
        CampaignProposal.belongsToMany(models.Resource, {
          through: models.CampaignProposalResource,
          foreignKey: 'proposalId'
        });
      }
    }
  });

  return CampaignProposal;
};
