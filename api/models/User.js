/**
 * User model
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

var bcrypt = require('bcryptjs');

module.exports = function(sequelize, DataTypes) {
  var User = sequelize.define('User', {
    id: {
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    email: {
      type: DataTypes.STRING,
      unique: true
    },
    role: {
      type: DataTypes.ENUM('admin', 'brand', 'influencer')
    },
    name: DataTypes.STRING,
    brandName: DataTypes.STRING,
    contactNumber: DataTypes.STRING,
    password: DataTypes.STRING,
    confirm: {
      type: DataTypes.BOOLEAN,
      defaultValue: false
    }
  }, {
    hooks: {
      beforeCreate: function(instance, options) {
        return instance.generateHash(instance.password).then(function(hashedPassword) {
          instance.password = hashedPassword;
          return options;
        })
      },
      beforeUpdate: function(instance, options) {
        if(instance.change('password')) {
          return instance.generateHash(instance.dataValues.password).then(function(hashedPassword) {
            instance.password = hashedPassword;
            return options;
          });
        }
      }
    },
    classMethods: {
      associate: function(models) {
        // payment
        User.belongsToMany(models.User, {
          through: models.Payment,
          as: 'source',
          foreignKey: 'sourceId',
          otherKey: 'targetId'
        });

        User.belongsToMany(models.User, {
          through: models.Payment,
          as: 'target',
          foreignKey: 'targetId',
          otherKey: 'sourceId'
        });

        // user has many permission groups
        User.belongsToMany(models.UserGroup, {
          through: models.UserUserGroup,
          as: 'users',
          foreignKey: 'userId',
          otherKey: 'groupId'
        });

        // brand has many campaigns
        User.hasMany(models.Campaign, {
          foreignKey: 'ownerId',
          scope: {
            role: 'brand'
          }
        });

        // inf belongs to many campaigns through application
        User.belongsToMany(models.Campaign, {
          foreignKey: 'userId',
          otherKey: 'campaignId',
          as: 'influencers',
          scope: {
            role: 'influencer'
          },
          through: models.CampaignApplication
        });

        // inf belongs to many campaigns through submission
        User.belongsToMany(models.Campaign, {
          foreignKey: 'userId',
          otherKey: 'campaignId',
          as: 'influencers',
          scope: {
            role: 'influencer'
          },
          through: models.CampaignSubmission
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
