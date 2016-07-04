/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var CampaignProposalResource = sequelize.define('CampaignProposalResource', {
    proposalId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'CampaignProposal',
        key: 'proposalId'
      }
    },
    resourceId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      references: {
        model: 'Resource',
        key: 'resourceId'
      }
    },
    isFeature: {
      type: DataTypes.BOOLEAN,
      defaultValue: false
    }
  }, {
    tableName: 'CampaignProposalResource',
    paranoid: true
  });

  return CampaignProposalResource;
};
