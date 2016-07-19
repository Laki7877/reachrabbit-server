/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var PaymentTransaction = sequelize.define('paymentTransaction', {
    transactionId: {
      type: DataTypes.UUID,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    status: {
      type: DataTypes.ENUM('pending','complete'),
      allowNull: false,
      defaultValue: 'pending'
    },
    description: {
      type: DataTypes.STRING
    },
    value: {
      type: DataTypes.DECIMAL(12,2),
      allowNull: false
    },
    paymentType: {
      type: DataTypes.ENUM('receive','pay','cancel'),
      allowNull: false
    },
    paymentMethod: {
      type: DataTypes.ENUM('bank transfer','cash'),
      allowNull: false
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
        PaymentTransaction.belongsTo(models.Resource, {
          foreignKey: 'resourceId'
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
