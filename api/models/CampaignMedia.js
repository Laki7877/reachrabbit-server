/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var CampaignMedia = sequelize.define('CampaignMedia', {
  }, {
    tableName: 'CampaignMedia'
  });

  return CampaignMedia;
};
