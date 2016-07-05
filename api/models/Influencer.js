/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var Influencer = sequelize.define('Influencer', {
    influencerId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      defaultValue: 'uuid_generate_v4()',
      primaryKey: true
    },
    userId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      references: {
        model: 'User',
        key: 'userId'
      }
    },
    gender: {
      type: DataTypes.ENUM('male','female','not specified"')
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
    bankId: {
      type: DataTypes.CHAR,
      references: {
        model: 'BankDetail',
        key: 'bankId'
      }
    },
    accountNumber: {
      type: DataTypes.STRING
    },
    createdBy: {
      type: DataTypes.STRING
    },
    createdAt: {
      type: DataTypes.DATE
    },
    updatedBy: {
      type: DataTypes.STRING
    },
    updatedAt: {
      type: DataTypes.DATE
    }
  }, {
    tableName: 'Influencer',
    paranoid: true
  });

  return Influencer;
};
