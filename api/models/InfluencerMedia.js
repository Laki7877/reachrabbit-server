/* jshint indent: 2 */
'use strict';
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
