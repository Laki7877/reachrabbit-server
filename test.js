var google = require('googleapis');
var yt = google.youtube('v3');
var OAuth2 = google.auth.OAuth2;

var yt = google.youtube('v3');

var oauth2Client = new OAuth2(
  '486841241364-75hb5e24afp7msiitf8t36skfo3mr0h7.apps.googleusercontent.com',
  '3NzypP4Nlzg7VuNVuhCJi_i5',
  'http://localhost:3000/auth/youtube');

var scopes = [
  'https://www.googleapis.com/auth/youtube'
];

var url = oauth2Client.generateAuthUrl({
      access_type: 'online',
      scope: scopes
});

console.log(url);
