/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  return sequelize.define('BankDetail', {
    bankId: {
      type: DataTypes.CHAR,
      allowNull: false,
      primaryKey: true
    },
    bankName: {
      type: DataTypes.STRING
    }
  }, {
    tableName: 'BankDetail',
    paranoid: true
  });
};
