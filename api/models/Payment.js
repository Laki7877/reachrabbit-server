/**
 * Payment model
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.2
 */
'use strict';

module.exports = function(sequelize, DataTypes) {
  var Payment = sequelize.define('Payment', {
    id: {
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    }
  }, {
    classMethods: {
      associate: function(models) {

      }
    }
  });
  return Payment;
};
