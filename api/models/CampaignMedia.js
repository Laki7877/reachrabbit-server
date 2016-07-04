/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  var CampaignMedia = sequelize.define('CampaignMedia', {
    campaignId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'Campaign',
        key: 'campaignId'
      }
    },
    mediaId: {
      type: DataTypes.UUIDV4,
      allowNull: false,
      references: {
        model: 'Media',
        key: 'mediaId'
      }
    }
  }, {
    tableName: 'CampaignMedia',
    paranoid: true
  });

  return CampaignMedia;
};
