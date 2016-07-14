/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var PaymentResource = sequelize.define('paymentResource', {
    isFeature: {
      type: DataTypes.BOOLEAN,
      allowNull: false,
      defaultValue: false
    }
  }, {
    tableName: 'PaymentResource',
    paranoid: true

  });

  return PaymentResource;
};
