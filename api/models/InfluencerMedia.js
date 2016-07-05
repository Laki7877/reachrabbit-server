/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var InfluencerMedia = sequelize.define('InfluencerMedia', {
    socialId: {
      type: DataTypes.STRING
    },
    token: {
      type: DataTypes.STRING
    }
  }, {
    tableName: 'InfluencerMedia'
  });

  return InfluencerMedia;
};
