/**
 * Campaign model
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.2
 */
'use strict';

module.exports = function(sequelize, DataTypes) {
  var Campaign = sequelize.define('Campaign', {
    id: {
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    title: DataTypes.STRING,
    description: {
      type: DataTypes.TEXT,
      length: 'long'
    },
    proposalDeadline: {
      type: DataTypes.DATE
    },
    submissionDeadline: {
      type: DataTypes.DATE
    },
    status: DataTypes.ENUM('draft', 'open', 'production', 'complete')
  }, {
    classMethods: {
      associate: function(models) {
        // brand owner
        Campaign.belongsTo(models.User, {
          foreignKey: 'ownerId'
        });

        // application
        Campaign.belongsToMany(models.User, {
          foreignKey: 'campaignId',
          otherKey: 'userId',
          scope: {
            role: 'influencer'
          },
          through: models.CampaignApplication
        });

        // submission
        Campaign.belongsToMany(models.User, {
          foreignKey: 'campaignId',
          otherKey: 'userId',
          scope: {
            role: 'influencer'
          },
          through: models.CampaignSubmission
        });
      }
    }
  });
  return Campaign;
};
