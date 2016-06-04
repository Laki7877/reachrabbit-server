/**
 * User model
 * 
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

// passport js
var passportLocalSequelize = require('passport-local-sequelize');

module.exports = function(sequelize, DataTypes) {
  var User = sequelize.define('User', {
    username: DataTypes.STRING,
    password: DataTypes.STRING,
    salt: DataTypes.STRING
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });

  // attach User to passportjs
  passportLocalSequelize.attachToUser(User, {
    usernameField: 'username',
    passwordField: 'password',
    saltField: 'salt'
  });
  
  return User;
};