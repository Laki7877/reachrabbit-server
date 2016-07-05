/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var CampaignProposal = sequelize.define('CampaignProposal', {
    proposalId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    campaignId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      references: {
        model: 'Campaign',
        key: 'campaignId'
      }
    },
    influencerId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      references: {
        model: 'Influencer',
        key: 'influencerId'
      }
    },
    description: {
      type: DataTypes.TEXT
    },
    proposePrice: {
      type: 'NUMERIC'
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
    paranoid: true
  });

  return CampaignProposal;
};
