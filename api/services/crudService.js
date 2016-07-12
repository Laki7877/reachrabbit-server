/**
 * A simple crud service that inherit/expose four methods:
 * create: Create an entity in database
 * read: Read an entity from database
 *
 *
 */
'use strict';
var models = require('../models'),
    Promise = require('bluebird');
/**
 * Constructor
 *
 * @class      Service CRUD
 * @param      {String}  modelName  The model name
 */
function Service(modelName) {
  if(_.isString(modelName)) {
    // modelName arg
    this.model = models[modelName];
    this.modelName = modelName;
  } else if(_.isObject(modelName)) {
    // model arg
    this.model = modelName;
    this.modelName = modelName.tableName;
  } else {
    throw new Error('Invalid model');
  }
}
/**
 * Create an entity
 *
 * @param      {Object}    entity  entity to create
 */
Service.prototype.create = function(entity) {
  // create entity
  var self = this;
  return new Promise(function(resolve, reject) {
    self.model.create(entity)
      .then(function(created) {
        resolve(created.get({plain:true}));
      }, reject);
  });
};
/**
 * Read an entity by id
 *
 * @param      {String}    id      The identifier
 */
Service.prototype.read = function(id) {
  var self = this;
  return new Promise(function(resolve, reject) {
    self.model.findById(id)
      .then(function(entity) {
        if(!entity) {
          reject();
        } else {
          resolve(entity);
        }
      }, reject);
  });
};
/**
 * Update an entity
 *
 * @param      {String}    id      The identifier
 * @param      {Object}    entity  The entity
 */
Service.prototype.update = function(id, entity) {
  var self = this;
  
  return async.waterfall([
    self.read(id),
    function(existingEntity) {
      // override fields
      _.extend(existingEntity, entity);

      // save to entity
      return existingEntity.save();
    }
  ]);
};
/**
 * Delete an entity
 *
 * @param      {String}    id      The identifier
 * @param      {Object}    entity  The entity
 */
Service.prototype.delete = function(id) {
  var self = this;
  return async.waterfall([
    // find current entity
    function() {
      return self.read(id);
    },
    // delete the entity
    function(existingEntity) {
      return existingEntity.destroy();
    }
  ]);
};
/**
 * Search for matches
 *
 * @param      {Object}  criteria  Where criteria
 * @param      {Object}  options   Sequelize options
 * @return     {Promise}  found object
 */
Service.prototype.find = function(criteria, options) {
  var self = this;
  return self.model.find(_.merge({}, options, { where: criteria}));
};
/**
 * Search for first match
 *
 * @param      {Object}  criteria  Where criteria
 * @param      {Object}  options   Sequelize options
 * @return     {Promise}  found object
 */
Service.prototype.findOne = function(criteria, options) {
  var self = this;
  return self.model.findOne(_.merge({}, options, { where: criteria }));
};

// module export
module.exports = function(model) {
  return new Service(model);
};
