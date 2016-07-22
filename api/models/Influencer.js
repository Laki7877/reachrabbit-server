/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var Influencer = sequelize.define('influencer', {
    influencerId: {
      type: DataTypes.UUID,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    userId: {
      type: DataTypes.UUID,
      allowNull: false,
      references: {
        model: 'User',
        key: 'userId'
      }
    },
    gender: {
      type: DataTypes.ENUM('male','female','not specified')
    },
    web: {
      type: DataTypes.STRING
    },
    about: {
      type: DataTypes.STRING
    },
    bankAccount: {
      type: DataTypes.STRING
    },
    createdBy: {
      type: DataTypes.STRING
    },
    updatedBy: {
      type: DataTypes.STRING
    }
  }, {
    tableName: 'Influencer',
    paranoid: true,
    classMethods: {
      associate: function(models) {
        Influencer.belongsTo(models.User, {
          foreignKey: 'userId'
        });
        Influencer.belongsToMany(models.Media, {
          through: models.InfluencerMedia,
          foreignKey: 'influencerId'
        });
        Influencer.hasMany(models.CampaignProposal, {
          foreignKey: 'influencerId'
        });
        Influencer.hasMany(models.CampaignSubmission, {
          foreignKey: 'influencerId'
        });
      }
    }
  });

  return Influencer;
};
