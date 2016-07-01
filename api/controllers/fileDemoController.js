'use strict';
var s3 = require('../services/S3Service.js'),
    fs = require('fs');

function listAll(req, res, next) {
  s3.list().then(res.send, next);
}

function uploadSingle(req, res, next) {
  var buffer = fs.readFileSync(req.file.path);
  var filename = req.file.originalname;
  s3.uploadPublic(buffer, filename, req.file.mimetype).then(res.send, next);
}

module.exports = {
  listAll: listAll,
  uploadSingle: uploadSingle
}
