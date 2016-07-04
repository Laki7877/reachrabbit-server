/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var Category = sequelize.define('Category', {
    categoryId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      defaultValue: 'uuid_generate_v4()',
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
    paranoid: true
  });

  return Category;
};
