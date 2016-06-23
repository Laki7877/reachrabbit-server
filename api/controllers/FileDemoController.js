'use strict';
var AWS = require('aws-sdk');
var s3 = new AWS.S3({
  accessKeyId: 'AKIAJSFCEJVD7YJ2BPZA',
  secretAccessKey: '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'
});

function listAll(req, res, next) {
  var params = {Bucket: 'projectxtest'};
  s3.listObjectsV2(params, function(err, data) {
    if(err) return next(err);
    res.send(data);
  });
}

function upload(req, res, next) {
   res.send('Successfully uploaded ' + req.files.length + ' files!')
}

module.exports = {
  listAll: listAll,
  upload: upload
}
