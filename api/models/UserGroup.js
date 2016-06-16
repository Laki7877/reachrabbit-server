/**
 * Usergroup model
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

module.exports = function(sequelize, DataTypes) {
  var UserGroup = sequelize.define('UserGroup', {
    id: {
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    }
  }, {
    classMethods: {
      associate: function(models) {
        UserGroup.belongsToMany(models.User, {
          through: models.UserUserGroup,
          as: 'groups',
          foreignKey: 'groupId',
          otherKey: 'userId'
        });
      }
    }
  });
  return UserGroup;
};