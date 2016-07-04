/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var Brand = sequelize.define('Brand', {
    brandId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    brandName: {
      type: DataTypes.STRING
    },
    createdBy: {
      type: DataTypes.STRING
    }
    updatedBy: {
      type: DataTypes.STRING
    }
  }, {
    tableName: 'Brand',
    paranoid: true,
    classMethods: {
      associate: function(models) {
        Brand.belongsTo(models.User, {
          foreignKey: 'userId'
        });
      }
    }
  });

  return Brand;
};
