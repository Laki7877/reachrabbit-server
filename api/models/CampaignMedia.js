/* jshint indent: 2 */
'use strict';
module.exports = function(sequelize, DataTypes) {
  var CampaignMedia = sequelize.define('CampaignMedia', {
  }, {
    tableName: 'CampaignMedia'
  });

  return CampaignMedia;
};
