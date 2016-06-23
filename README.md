# projectx-server

## Usage

1. copy and rename .env-sample to .env
2. fill in environment variable
```
  npm install
  gulp db:sync
  gulp server
```

## Regenerate Database from model files
```
  gulp db:drop
  gulp db:sync
```
