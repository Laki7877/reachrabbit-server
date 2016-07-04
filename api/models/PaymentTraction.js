/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var PaymentTraction = sequelize.define('PaymentTraction', {
    tractionId: {
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
    userId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      references: {
        model: 'User',
        key: 'userId'
      }
    },
    paymentType: {
      type: DataTypes.ENUM('receive','pay','cancel'),
      allowNull: true
    },
    paymentMethod: {
      type: DataTypes.ENUM('bank transfer','cash'),
      allowNull: true
    },
    createdBy: {
      type: DataTypes.STRING,
      allowNull: true
    },
    createdAt: {
      type: DataTypes.DATE,
      allowNull: true
    },
    updatedBy: {
      type: DataTypes.STRING,
      allowNull: true
    },
    updatedAt: {
      type: DataTypes.DATE,
      allowNull: true
    }
  }, {
    tableName: 'PaymentTraction',
    paranoid: true
  });

  return PaymentTraction;
};
