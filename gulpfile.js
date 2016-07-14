/**
 * gulp entry point
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */
'use strict';

require('dotenv-extended').load();

var _       = require('lodash'),
  fs        = require('fs'),
  args      = require('yargs').argv,
  gulp      = require('gulp-help')(require('gulp')),
  guppy     = require('git-guppy')(gulp),
  path      = require('path'),
  fixtures  = require('sequelize-fixtures'),
  _exec     = require('child_process').exec,
  plugins   = require('gulp-load-plugins')({
      pattern: ['gulp-*', 'gulp.*'],
      replaceString: /\bgulp[\-.]/
  }),
  args      = process.argv.slice(3),
  argv      = require('yargs').argv,
  db        = require('./api/models'),
  sequelizeCliPath = './node_modules/sequelize-cli/bin/sequelize';


/***************************************************
 * Helper functions
 ***************************************************/

// exec with piped output
function exec(cmd, obj, done) {
  var task = _exec(cmd, obj, done);
  task.stderr.pipe(process.stderr);
  task.stdout.pipe(process.stdout);
}

// exec with sequelize cli
function seqExec(cmd, done) {
  var cli = args.slice().map(function(e) {
    if(_.indexOf(e, ' ') >= 0) {
      return '"' + e + '"';
    }
    return e;
  }).join(' ');
  exec('node ' + sequelizeCliPath + ' ' + cmd + ' ' + cli, {}, done);
}

// pass gulp task directly to seqExec
var seqExecTask = function(done) {
  seqExec(this.seq.slice(-1)[0], done);
};

/***************************************************
 * Git hook
 ***************************************************/

// git precommit
gulp.task('pre-commit', 'Git hook pre-commit', ['lint']);

/***************************************************
 * Test
 ***************************************************/

// pre test
gulp.task('pre-test', 'Setup pretest routine', function() {
  // test mode
  process.env.NODE_ENV = 'test';

  return gulp.src(['api/**/*.js'])
    .pipe(plugins.istanbul())
    .pipe(plugins.istanbul.hookRequire());
});

// test
gulp.task('test', 'Run mocha test API', ['pre-test'], function() {
  var opts = {
    reporter: 'mocha-better-spec-reporter',
    timeout: 60000,
    require: ['./test/common/init.js']
  };

  return gulp.src(['test/**/*.spec.js'])
    .pipe(plugins.mocha(opts))
    .pipe(plugins.istanbul.writeReports())
    .pipe(plugins.istanbul.enforceThresholds({thresholds: {global: 90}}))
    .once('error', function(err) {
      process.exit(1);
    })
    .once('end', function() {
      process.exit();
    });
});

/***************************************************
 * Database
 ***************************************************/

// mimick sequelize cli
gulp.task('db:migrate', 'Run pending migrations', seqExecTask);
gulp.task('db:migrate:undo', 'Revert last migration ran', seqExecTask);
gulp.task('db:migrate:undo:all', 'Revert all migration ran', seqExecTask);
gulp.task('db:migrate:create', 'Create migration file', seqExecTask, {
  options: {
    name: 'Migration file name'
  }
});

gulp.task('db:sync', 'Sync all tables to sequelize models', function() {
  return db.sequelize.sync({ force: true })
    .then(function() {
      return fixtures.loadFile('api/seeders/**/*.json', db);
    });
});

gulp.task('db:drop', 'Drop all tables', function() {
  return db.sequelize.drop({cascade: true});
});

gulp.task('db:seed', 'Seed all table', function() {
  return fixtures.loadFile('api/seeders/**/*.json', db);
});


/***************************************************
 * Application
 ***************************************************/

// lint
gulp.task('lint', 'Lint all server side js', function() {
  return gulp.src(['./**/*.js', '!./node_modules/**/*.js', '!./coverage/**/*.js'])
    .pipe(plugins.jshint())
    .pipe(plugins.jshint.reporter(require('jshint-stylish')));
});

// start express server
gulp.task('server', 'Run express server', function() {
  // start express server
  var server = plugins.liveServer.new('app.js');
  server.config.livereload.port = 30000;
  server.start();

  // notify server for livereload
  gulp.watch([path.resolve(__dirname, 'api/**/*.js')], function(file) {
    server.start.bind(server)();
  });
}, {
  aliases: ['start', 'run', 'serve', 'watch']
});

// bump version
gulp.task('bump', 'Update repository semver version (X.X.X)', function() {
  // should only be either of these values
  // otherwise, default to "patch"
  var version = _.includes(['patch', 'minor', 'major', 'prerelease'], args.version) ? args.version : 'patch';

  return gulp.src('./package.json')
    .pipe(plugins.bump({type: version}))
    .pipe(gulp.dest('./'));
}, {
  options: {
    version: 'One of bump version type (minor, major, patch, prerelease). default: patch'
  }
});
