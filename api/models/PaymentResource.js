/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var PaymentResource = sequelize.define('PaymentResource', {
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
