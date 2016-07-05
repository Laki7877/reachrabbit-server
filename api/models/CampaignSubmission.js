/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  return sequelize.define('CampaignSubmission', {
    submissionId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      defaultValue: 'uuid_generate_v4()',
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
    influncerId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      references: {
        model: 'Influencer',
        key: 'influencerId'
      }
    },
    title: {
      type: DataTypes.STRING,
      allowNull: true
    },
    description: {
      type: DataTypes.TEXT,
      allowNull: true
    },
    createdBy: {
      type: DataTypes.STRING,
      allowNull: true
    },
    updatedBy: {
      type: DataTypes.STRING,
      allowNull: true
    }
  }, {
    tableName: 'CampaignSubmission',
    paranoid: true
  });
};
