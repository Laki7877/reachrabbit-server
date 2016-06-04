/**
 * gulp entry point
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @since      0.0.1
 */

var _ = require('lodash'),
	gulp = require('gulp-help')(require('gulp')),
	gutil = require('gulp-util'),
	path = require('path'),
	exec = require('child_process').exec;

// lint
gulp.task('lint', 'Lint all server side js', function() {
	var jshint = require('gulp-jshint');
	var stylish = require('jshint-stylish');
	
	gulp.src('./api/**/*.js')
		.pipe(jshint())
		.pipe(jshint.reporter(stylish));
})

// run express-swagger app
gulp.task('start', 'Start express app', function(cb) {
	var task = exec('swagger project start', {}, function(err) {
		cb(err);
	});
	task.stderr.pipe(process.stderr);
	task.stdout.pipe(process.stdout);
});

// run swagger edit mode
gulp.task('edit', 'Start swagger yaml edit mode', function(cb) {
	var task = exec('swagger project edit', {}, function(err) {
		cb(err);
	});
	task.stderr.pipe(process.stderr);
	task.stdout.pipe(process.stdout);
})