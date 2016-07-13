/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var Campaign = sequelize.define('Campaign', {
    campaignId: {
      type: DataTypes.UUID,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    brandId: {
      type: DataTypes.UUID,
      allowNull: false,
      references: {
        model: 'Brand',
        key: 'brandId'
      }
    },
    categoryId: {
      type: DataTypes.UUID,
      allowNull: false,
      references: {
        model: 'Category',
        key: 'categoryId'
      }
    },
    title: {
      type: DataTypes.STRING
    },
    description: {
      type: DataTypes.TEXT
    },
    proposalDeadline: {
      type: DataTypes.DATE
    },
    submissionDeadline: {
      type: DataTypes.DATE
    },
    status: {
      type: DataTypes.ENUM('draft','open','production','complete','delete')
    },
    createdBy: {
      type: DataTypes.STRING
    },
    updatedBy: {
      type: DataTypes.STRING
    }
  }, {
    tableName: 'Campaign',
    paranoid: true,
    classMethods: {
      associate: function(models) {
        Campaign.belongsTo(models.Brand, {
          foreignKey: 'brandId'
        });
        Campaign.belongsTo(models.Category, {
          foreignKey: 'categoryId'
        });
        Campaign.belongsToMany(models.Media, {
          through: models.CampaignMedia
        });
        Campaign.belongsToMany(models.Resource, {
          through: models.CampaignResource,
          foreignKey: 'campaignId'
        });
        Campaign.hasMany(models.PaymentTransaction, {
          foreignKey: 'campaignId'
        });
      }
    }
  });

  return Campaign;
};
