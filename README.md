# projectx-server

## Usage

1. copy and rename .env-sample to .env
2. fill in environment variable
```
  npm install
  gulp db:sync
  gulp server
```
### Mail Service Usage

```
var M = require('../services/mailService.js');
M.send('tony@stark.com', 'Welcome to Project X', 'email_confirmation', {name: 'Tony Stark'})
.then(function(){
    //done
});
```
The send method takes the following arguments 

```
  send(receipient_email, subject, template_name, parameters);
```

Templates are located under `email_templates` directory. 


## Regenerate Database from model files
```
  gulp db:drop
  gulp db:sync
```
