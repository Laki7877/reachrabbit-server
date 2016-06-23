var AWS = require('aws-sdk');
var s3 = new AWS.S3({
  accessKeyId: 'AKIAJSFCEJVD7YJ2BPZA',
  secretAccessKey: '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'
});

module.exports = s3;
