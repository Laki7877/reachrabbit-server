/**
 * Usergroup model
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

module.exports = function(sequelize, DataTypes) {
  var UserUserGroup = sequelize.define('UserUserGroup', {
    id: {
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    }
  }, {
    classMethods: {
      associate: function(models) {
      }
    }
  });
  return UserUserGroup;
};