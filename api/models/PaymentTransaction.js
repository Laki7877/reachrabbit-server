/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var PaymentTransaction = sequelize.define('PaymentTransaction', {
    transactionId: {
      type: DataTypes.UUID,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
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
    updatedBy: {
      type: DataTypes.STRING,
      allowNull: true
    }
  }, {
    tableName: 'PaymentTransaction',
    paranoid: true,
    classMethods: {
      associate: function(models) {
        PaymentTransaction.belongsToMany(models.Resource, {
          through: models.PaymentResource,
          foreignKey: 'transactionId'
        });
        PaymentTransaction.belongsTo(models.User, {
          foreignKey: 'userId'
        });
        PaymentTransaction.belongsTo(models.Campaign, {
          foreignKey: 'campaignId'
        });
      }
    }
  });

  return PaymentTransaction;
};
