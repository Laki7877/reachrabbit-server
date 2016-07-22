/**
 * Handle file upstream and downstream with S3
 *
 * @since      0.0.1
 */
'use strict';
var s3 = require('../services/staticFileService'),
    fs = require('fs'),
    request = require('request'),
    Resource = require('../models').Resource;
var md5 = require('md5');

module.exports = {
  listAll: function(req, res, next) {
    s3.list().then(function(data){
      res.send(data);
    }, next);
  },
  /*
   * Upload from remote URL
   */
  fromRemote: function(req, res, next){
      var urls = req.body.url.split("?");
      var newFn = s3.generateResourceId(urls[0]);
      request({
        url: req.body.url,
        encoding: null
      }, function (error, response, buffer) {
        console.log(buffer instanceof Buffer);
        s3.uploadPublic(buffer, newFn, req.body.mimetype).then(function(d){
          Resource.create({
            resourcePath: newFn,
            resourceType: 'image',
            createdBy: _.get(req.user, 'email')
          })
          .then(function(resourceInstance){
              var resource = resourceInstance.get({ plain: true });
              // delete tmp file
              /*
                fs.exists(req.file.path, function(exists) {
                  if(exists) {
                    fs.unlink(req.file.path);
                  }
                });
              */
              // send resource
              res.send(_.merge({
                url : process.env.S3_PUBLIC_URL + newFn
              }, resource ));
          });
        });
      });
  },

  /*
  * Upload from local file
  */
  uploadSingle: function(req, res, next) {
    var buffer = fs.readFileSync(req.file.path);
    var filename = s3.generateResourceId(req.file.originalname);
    s3.uploadPublic(buffer, filename, req.file.mimetype).then(function(d){
      Resource.create({
        resourcePath: filename,
        resourceType: 'image',
        createdBy: _.get(req.user, 'email')
      }).then(function(resourceInstance){
          var resource = resourceInstance.get({ plain: true });
          // delete tmp file
          fs.exists(req.file.path, function(exists) {
            if(exists) {
              fs.unlink(req.file.path);
            }
          });
          // send resource
          res.send(_.merge({
            url : process.env.S3_PUBLIC_URL + filename
          }, resource ));
      });

    }, function(err){
      next(err);
    });
  }
};
