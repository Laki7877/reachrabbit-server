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
    comment: {
      type: DataTypes.TEXT
    },
    proposePrice: {
      type: DataTypes.DECIMAL(12,2),
      allowNull: false
    },
    status: {
      type: DataTypes.ENUM('wait for review','reject','need revision', 'accept'),
      defaultValue: 'wait for review',
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
        CampaignProposal.hasMany(models.CampaignSubmission, {
          foreignKey: 'proposalId'
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
