/**
 * A simple crud service that inherit/expose four methods:
 * create: Create an entity in database
 * read: Read an entity from database
 *
 *
 */
'use strict';
var models = require('../models');

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
 * @param      {Function}  done    The done
 */
Service.prototype.create = function(entity, done) {
  // create entity
  this.model.create(entity).then(function(created) {
    done(null, created);
  }).catch(done);
};

/**
 * Read an entity by id
 *
 * @param      {String}    id      The identifier
 * @param      {Function}  done    The done
 */
Service.prototype.read = function(id, done) {
  var self = this;
  this.model.findById(id).then(function(entity) {
    if(!entity) {
      return done(new errors.NotFoundError(self.modelName + ' not found with the given id'));
    }
    done(null, entity);
  }).catch(done);
};

/**
 * Update an entity
 *
 * @param      {String}    id      The identifier
 * @param      {Object}    entity  The entity
 * @param      {Function}  done    The done
 */
Service.prototype.update = function(id, entity, done) {
  var self = this;
  async.waterfall([
    // find current entity
    function(cb) {
      self.read(id, cb);
    },
    // update with new values
    function(existingEntity, cb) {
      _.extend(existingEntity, entity);
      existingEntity.save().then(function(updatedEntity) {
        cb(null, updatedEntity);
      }).catch(cb);
    }
  ], done);
};

/**
 * Delete an entity
 *
 * @param      {String}    id      The identifier
 * @param      {Object}    entity  The entity
 * @param      {Function}  done    The done
 */
Service.prototype.delete = function(id, done) {
  var self = this;
  async.waterfall([
    // find current entity
    function(cb) {
      self.read(id, cb);
    },
    // delete the entity
    function(existingEntity, cb) {
      existingEntity.destroy().then(function() {
        cb();
      }).catch(cb);
    }
  ], done);
};

// module export
module.exports = function(model) {
  return new Service(model);
};
