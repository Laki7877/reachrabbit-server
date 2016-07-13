/* jshint indent: 2 */
'use strict';
var bcrypt = require('bcryptjs'),
  Promise = require('bluebird');

module.exports = function(sequelize, DataTypes) {
  var User = sequelize.define('User', {
    userId: {
      type: DataTypes.UUID,
      allowNull: false,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    name: {
      type: DataTypes.STRING
    },
    email: {
      type: DataTypes.STRING
    },
    password: {
      type: DataTypes.STRING
    },
    profilePicture: {
      type: DataTypes.UUID,
      references: {
        model: 'Resource',
        key: 'resourceId'
      }
    },
    contactNumber: {
      type: DataTypes.STRING
    },
    createdBy: {
      type: DataTypes.STRING
    },
    updatedBy: {
      type: DataTypes.STRING
    }
  }, {
    tableName: 'User',
    paranoid: true,
    hooks: {
      beforeCreate: function(instance, options) {
        if(instance.password) {
          return instance.generateHash(instance.password).then(function(hashedPassword) {
            instance.password = hashedPassword;
            return options;
          });
        } else {
          return options;
        }
      },
      beforeUpdate: function(instance, options) {
        if(instance.change('password')) {
          return instance.generateHash(instance.dataValues.password).then(function(hashedPassword) {
            instance.password = hashedPassword;
            return options;
          });
        }
        return options;
      }
    },
    classMethods: {
      associate: function(models) {
        // roles
        User.hasOne(models.Brand, {
          foreignKey: 'userId'
        });
        User.hasOne(models.Influencer, {
          foreignKey: 'userId'
        });

        // bank
        User.belongsTo(models.Bank, {
          foreignKey: 'bankId'
        });
        User.hasMany(models.PaymentTransaction, {
          foreignKey: 'userId'
        });
      }
    },
    instanceMethods: {
      generateHash: function(password) {
        var hash = Promise.promisify(bcrypt.hash, { context: bcrypt });
        return hash(password, 10);
      },
      verifyPassword: function(password) {
        var compare = Promise.promisify(bcrypt.compare, { context: compare });
        return compare(password, this.password);
      }
    }
  });
  return User;
};
