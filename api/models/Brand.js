/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var Brand = sequelize.define('Brand', {
    brandId: {
      type: DataTypes.UUID,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    userId: {
      type: DataTypes.UUID,
      allowNull: false,
      references: {
        model: 'User',
        key: 'userId'
      }
    },
    brandName: {
      type: DataTypes.STRING
    },
    createdBy: {
      type: DataTypes.STRING
    },
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
        Brand.hasMany(models.Campaign, {
          foreignKey: 'brandId'
        });
      }
    }
  });

  return Brand;
};
