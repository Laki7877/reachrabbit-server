'use strict';
var s3 = require('../services/S3Service.js');

function listAll(req, res, next) {
  var params = {Bucket: 'projectxtest'};
  s3.listObjectsV2(params, function(err, data) {
    if(err) return next(err);
    res.send(data);
  });
}

function uploadSingle(req, res, next) {
  var params = {Bucket: 'projectxtest', Key: 'fileName', Body: new Buffer(req.file)};
  s3.putObject(params, function(err, data) {
    res.send(data);
  });
}

function getOne(req, res, next) {
  var params = {Bucket: 'projectxtest', Key: req.params.id};
  s3.getObject(params, function(err, data) {
    res.send(data);
  });
}

module.exports = {
  listAll: listAll,
  uploadSingle: uploadSingle,
  getOne: getOne
}
