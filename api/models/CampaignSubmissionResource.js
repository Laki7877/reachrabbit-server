/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  return sequelize.define('CampaignSubmissionResource', {
    submissionId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'CampaignSubmission',
        key: 'submissionId'
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
      allowNull: false,
      defaultValue: false
    }
  }, {
    tableName: 'CampaignSubmissionResource',
    paranoid: true
  });
};
