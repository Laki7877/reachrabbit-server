/**
 * Provide S3 handling methods and wrapper
 *
 * @author     Pat Sabpisal <ssabpisa@me.com>
 * @since      0.0.1
 */
'use strict';

var AWS = require('aws-sdk');
var Promise = require('bluebird');
var md5 = require('md5');

var s3 = new AWS.S3({
  accessKeyId: process.env.S3_ACCESS_KEY_ID,
  secretAccessKey: process.env.S3_SECRET_ACCESS_KEY
});

var bucketName = process.env.S3_BUCKET_NAME;

module.exports = {
  /*
  * generate preferably unique filename
  */
  generateResourceId: function(originalFileName){
    var exts = originalFileName.split('.');
    var ext = exts[exts.length - 1];
    return md5(originalFileName, Date.now()) + '.' + ext;
  },
  /*
  * List file at path
  * {path} [String] - Path to list [NOT IMPLEMENTED]
  */
  list: function(path) {
    var promise = new Promise(function(resolve, reject) {
      var params = {
        Bucket: bucketName
      };
      s3.listObjectsV2(params, function(err, data) {
        if (err) return reject(err);
        resolve(data);
      });
    });
    return promise;
  },
  getOne: function(filePath){
    throw 'Not IMPLEMENTED';
  },
  /*
  * Upload to public. Anyone with link can view.
  */
  uploadPublic: function(buffer, filename, mimetype){
    var params = {
      Bucket: bucketName,
      Key: filename,
      Body: buffer,
      ACL: 'public-read',
      ContentType: mimetype
    };

    var promise = new Promise(function(resolve, reject) {
      s3.putObject(params, function(err, data) {
        if (err) return reject(err);
        resolve(data);
      });
    });
    return promise;

  }
};
