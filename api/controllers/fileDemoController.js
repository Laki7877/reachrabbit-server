'use strict';
var s3 = require('../services/staticFileService'),
    fs = require('fs'),
    Resource = require('../models').Resource;

function listAll(req, res, next) {
  s3.list().then(function(data){
    res.send(data);
  }, next);
}

function uploadSingle(req, res, next) {
  console.log(req.file)
  var buffer = fs.readFileSync(req.file.path);
  var filename = s3.generateResourceId(req.file.originalname);
  console.log("uploading", filename)
  s3.uploadPublic(buffer, filename, req.file.mimetype).then(function(d){
    Resource.create({
      resourcePath: filename,
      resourceType: 'image',
      createdBy: _.get(req.user, 'email')
    })
    res.send({
      url : process.env.S3_PUBLIC_URL + filename
    });
  }, function(err){
    console.log(err);
    next(err);
  });
}

module.exports = {
  listAll: listAll,
  uploadSingle: uploadSingle
}
