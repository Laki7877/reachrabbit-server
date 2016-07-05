/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var Media = sequelize.define('Media', {
    mediaId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      defaultValue: 'uuid_generate_v4()',
      primaryKey: true
    },
    mediaName: {
      type: DataTypes.STRING
    },
    isActive: {
      type: DataTypes.BOOLEAN,
      defaultValue: false
    }
  }, {
    tableName: 'Media',
    paranoid: true
  });

  return Media;
};
