/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var PaymentResource = sequelize.define('PaymentResource', {
    tractionId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'PaymentTraction',
        key: 'tractionId'
      }
    },
    resourceId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      references: {
        model: 'Resource',
        key: 'resourceId'
      }
    },
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
