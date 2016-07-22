/**
 * Provide mailing method
 *
 * @author     Pat Sabpisal <ssabpisa@me.com>
 * @since      0.0.1
 */
'use strict';

var apiKey = process.env.MAILGUN_KEY || 'key-16698456d07574ddd7e8c86b075e248c';
var domain = process.env.MAILGUN_DOMAIN || 'unishift.co';
var sendAs = 'sansa@' + domain;
var Mailgun = require('mailgun-js');
var Promise = require('bluebird');
var mailgun = new Mailgun({apiKey: apiKey, domain: domain});
var Handlebars = require('handlebars');
var $templates = require('../email_templates');

var mailshooter = {
  /*
  * {emails} [List<String>] - list fo emails to be added
  * {listName} [String] - name of the mailing list
  */
  addToMailingList: function(listName, emails){
    throw 'Not implemented yet bitch';
  },
  /*
  * send using template in email_template folder
  * {to} [String] - receipient email,
  * {subject} [String] - subject
  * {template_name} [String] - Template name
  * {paramters} [Object] - parameters to pass to template
  */
  send: function(to, subject, template_name, parameters){
    if(!$templates[template_name]){
        throw 'Unable to find template: ' + template_name;
    }

    var template = Handlebars.compile($templates[template_name]);

    return mailshooter._send(to, subject, template(parameters));
  },
  /*
  * raw email send
  * {to} [String] - receipient email,
  * {subject} [String] - subject
  * {message} [String] - HTML
  */
  _send: function(to, subject, message){

    var promise = new Promise(function(resolve, reject) {

        var data = {
          from: sendAs,
          to: to,
          subject: subject,
          html: message
        };

        mailgun.messages().send(data, function(err,body){
          if(err){
            reject(err);
          }
          resolve(body);
        });

    });
    return promise;
  }
};

module.exports = mailshooter;
