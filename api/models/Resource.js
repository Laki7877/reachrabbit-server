/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var Resource = sequelize.define('Resource', {
    resourceId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      defaultValue: 'uuid_generate_v4()',
      primaryKey: true
    },
    resourcePath: {
      type: DataTypes.STRING
    },
    resourceType: {
      type: DataTypes.ENUM('pic','vdo')
    },
    createdBy: {
      type: DataTypes.STRING
    },
    createdAt: {
      type: DataTypes.DATE
    },
    updatedBy: {
      type: DataTypes.STRING
    },
    updatedAt: {
      type: DataTypes.DATE
    }
  }, {
    tableName: 'Resource',
    paranoid: true
  });

  return Resource;
};
