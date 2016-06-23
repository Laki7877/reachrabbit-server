var path = require("path");
var normalizedPath = path.join(__dirname);
var fs = require('fs');
var templates = {};

fs.readdirSync(normalizedPath)
.forEach(function(file) {
  var content = fs.readFileSync(path.join(__dirname, file), {
    'encoding': 'utf-8'
  });
  templates[file.replace(/\.handlebars/, '')] = content;
});

module.exports = templates;
