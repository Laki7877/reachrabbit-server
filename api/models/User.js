/* jshint indent: 2 */

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
    role: {
      type: DataTypes.ENUM('admin','brand','influencer')
    },
    email: {
      type: DataTypes.STRING
    },
    password: {
      type: DataTypes.STRING
    },
    picture: {
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
    classMethods: {
      associate: function(models) {
        User.hasOne(models.Brand, {
          foreignKey: 'userId'
        });
        User.hasOne(models.Influencer, {
          foreignKey: 'userId'
        });
        User.belongsTo(models.Bank, {
          foreignKey: 'bankId'
        });
        User.hasMany(models.PaymentTransaction, {
          foreignKey: 'userId'
        });
      }
    },
    paranoid: true
  });
  return User;
};
