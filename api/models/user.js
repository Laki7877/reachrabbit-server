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
    password: DataTypes.STRING,
    facebook: DataTypes.STRING,
    token: DataTypes.STRING
  }, {
    hooks: {
      beforeBulkCreate: function(models, options, done) {
        hashPasswords(models, options, done);
      },
      beforeBulkUpdate: function(models, options, done) {
        hashPasswords(models, options, done);
      },
      beforeCreate: function(model, options, done) {
        hashPassword(model, options, done);
      },
      beforeUpdate: function(model, options, done) {
        hashPassword(model, options, done);
      }
    },
    classMethods: {
      associate: function(models) {
        User.belongsToMany(models.UserGroup, {
          through: models.UserUserGroup,
          as: 'users',
          foreignKey: 'userId',
          otherKey: 'groupId'
        });
      }
    },
    instanceMethods: {
      generateHash: function(password, done) {
        return bcrypt.hash(password, 10, done);
      },
      verifyPassword: function(password, done) {
        return bcrypt.compare(password, this.password, done);
      }
    }
  });

  return User;
};


/**
 * Hash password with model generateHash
 *
 * @param      {object}    model    The model
 * @param      {object}    options  The options
 * @param      {Function}  done     The done
 */
function hashPassword(model, options, done) {
  if(!_.isNil(model.password)) {
    model.generateHash(model.password, function(err, encryptedPassword) {
      if(err) {
        return done(err);
      }
      model.password = encryptedPassword;
      return done(null, options);
    });
  } else {
    return done(null, options);
  }
}

/**
 * Hash password for array of models
 *
 * @param      {array}    models   The models
 * @param      {object}    options  The options
 * @param      {Function}  done     The done
 */
function hashPasswords(models, options, done) {
  var tasks = [];
  // build hash password tasks for all models
  _.forEach(models, function(model) {
    tasks.push(function(cb) {
      hashPassword(model, options, cb);
    });
  });
  return async.parallel(tasks, function(err, results) {
    if(err) {
      return done(err);
    }
    return done(null, options);
  });
}
