/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var Influencer = sequelize.define('Influencer', {
    influencerId: {
      type: DataTypes.UUID,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    gender: {
      type: DataTypes.ENUM('male','female','not specified')
    },
    facebookId: {
      type: DataTypes.STRING
    },
    facebookToken: {
      type: DataTypes.STRING
    },
    youtubeId: {
      type: DataTypes.STRING
    },
    youtubeToken: {
      type: DataTypes.STRING
    },
    instagramId: {
      type: DataTypes.STRING
    },
    instagramToken: {
      type: DataTypes.STRING
    },
    web: {
      type: DataTypes.STRING
    },
    aboutYourself: {
      type: DataTypes.STRING
    },
    accountNumber: {
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
      }
    }
  });

  return Influencer;
};
