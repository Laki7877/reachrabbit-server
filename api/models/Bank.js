/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var Bank = sequelize.define('Bank', {
    bankId: {
      type: DataTypes.CHAR,
      allowNull: false,
      primaryKey: true
    },
    bankName: {
      type: DataTypes.STRING
    }
  }, {
    tableName: 'Bank',
    paranoid: true,
    classMethods: {
      associate: function(models) {
        Bank.hasOne(models.User, {
          foreignKey: 'bankId'
        });
      }
    }
  });

  return Bank;
};
