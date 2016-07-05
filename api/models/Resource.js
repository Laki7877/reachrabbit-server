/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var Resource = sequelize.define('Resource', {
    resourceId: {
      type: DataTypes.UUID,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    resourcePath: {
      type: DataTypes.STRING
    },
    resourceType: {
      type: DataTypes.ENUM('image','video', 'binary')
    },
    createdBy: {
      type: DataTypes.STRING
    },
    updatedBy: {
      type: DataTypes.STRING
    }
  }, {
    tableName: 'Resource',
    paranoid: true,
    classMethods: {
      associate: function(models) {
        Resource.belongsToMany(models.Campaign, {
          through: models.CampaignResource,
          foreighKey: 'resourceId',
          otherKey: 'campaignId'
        });
        Resource.belongsToMany(models.CampaignProposal, {
          through: models.CampaignProposalResource,
          foreighKey: 'resourceId',
          otherKey: 'proposalId'
        });
        Resource.belongsToMany(models.CampaignSubmission, {
          through: models.CampaignSubmissionResource,
          foreighKey: 'resourceId',
          otherKey: 'submissionId'
        });
        Resource.belongsToMany(models.PaymentTransaction, {
          through: models.PaymentResource,
          foreighKey: 'resourceId',
          otherKey: 'transactionId'
        });
      }
    }
  });

  return Resource;
};
