'use strict';
var s3 = require('../services/S3Service.js'),
    fs = require('fs');
function listAll(req, res, next) {
  var params = {Bucket: 'projectxtest'};
  s3.listObjectsV2(params, function(err, data) {
    if(err) return next(err);
    res.send(data);
  });
}

function uploadSingle(req, res, next) {
  var buffer = fs.readFileSync(req.file.path);
  var filename = req.file.originalname;
  var params = {
    Bucket: 'projectxtest',
    Key: filename,
    Body: buffer,
    ACL: 'public-read',
    ContentType: req.file.mimetype
  };
  s3. putObject(params, function(err, data) {
    res.json({
      url: 'https://s3-ap-southeast-1.amazonaws.com/projectxtest/' + filename
    });
  });
}

function getOne(req, res, next) {
  var params = {Bucket: 'projectxtest', Key: req.params.id};
  s3.getObject(params, function(err, data) {
    if(err) {
      return next(err);
    }
    res.send(data);
  });
}

module.exports = {
  listAll: listAll,
  uploadSingle: uploadSingle,
  getOne: getOne
}
