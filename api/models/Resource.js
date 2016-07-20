/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var Resource = sequelize.define('resource', {
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
      type: DataTypes.ENUM('image','video', 'binary', 'link')
    },
    createdBy: {
      type: DataTypes.STRING
    },
    updatedBy: {
      type: DataTypes.STRING
    },
    url: {
      type: DataTypes.VIRTUAL,
      get: function() {
        return process.env.S3_PUBLIC_URL + this.get('resourcePath');
      }
    }
  }, {
    tableName: 'Resource',
    paranoid: true,
    classMethods: {
      associate: function(models) {
        Resource.belongsToMany(models.Campaign, {
          through: models.CampaignResource,
          foreighKey: 'resourceId',
        });
        Resource.belongsToMany(models.CampaignProposal, {
          through: models.CampaignProposalResource,
          foreighKey: 'resourceId',
        });
        Resource.belongsToMany(models.CampaignSubmission, {
          through: models.CampaignSubmissionResource,
          foreighKey: 'resourceId',
        });
        /*
        Resource.belongsToMany(models.CampaignSubmission, {
          through: models.CampaignSubmissionProof,
          foreighKey: 'resourceId',
          otherKey: 'submissionId',
          as: ''
        });*/
      }
    }
  });

  return Resource;
};
