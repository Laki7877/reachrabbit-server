/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var CampaignProposal = sequelize.define('CampaignProposal', {
    proposalId: {
      type: DataTypes.UUID,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    description: {
      type: DataTypes.TEXT
    },
    proposePrice: {
      type: DataTypes.DECIMAL(12,2)
    },
    status: {
      type: DataTypes.ENUM('propose','reject','needrevision')
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
        CampaignProposal.belongsToMany(models.Resource, {
          through: models.CampaignProposalResource,
          foreignKey: 'proposalId'
        });
      }
    }
  });

  return CampaignProposal;
};
