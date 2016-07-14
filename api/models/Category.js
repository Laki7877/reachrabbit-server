/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var Category = sequelize.define('category', {
    categoryId: {
      type: DataTypes.UUID,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    categoryName: {
      type: DataTypes.STRING,
    },
    isActive: {
      type: DataTypes.BOOLEAN,
      defaultValue: false
    }
  }, {
    tableName: 'Category',
    paranoid: true,
    classMethods: {
      associate: function(models) {
      }
    }
  });

  return Category;
};
