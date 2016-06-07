# Project X server

A Node-express-swagger RESTful API server with Sequelize ORM and Postgres integration.


## Installation

Assuming you already have latest [node](https://nodejs.org) installed

```
  npm install -g gulp-cli swagger
  npm install
```

## Quick start

Assuming you have appropriate database setup. If not, I recommended [PostgreSQL](www.postgresql.org).

1. Copy and rename `.env-sample` to `.env`
2. Fill in `.env`
3. `gulp db:migrate`
4. `gulp start`

## Editing Swagger API

You can directly edit `./swagger/swagger.yaml` or run `gulp edit` to open up builtin editor. Please note that you have to run separate server process (`gulp start`) to be able to test API via swagger UI editor.

## Additional Informations

Run `gulp help` to see all available tasks

## Database Engine

This project natively with [PostgreSQL](www.postgresql.org) package. However, `sequelize` also support other database dialect: `mysql`, `sqlite`, `postgres`, and `mssql`. 

```
	npm install pg pg-hstore // Postgres
	npm install mysql // MySQL
	npm install sqlite3 // SQLite
	npm install tedious // MSSQL
```

Please refer to [Sequelize Guide](http://docs.sequelizejs.com/en/latest/docs/getting-started/)

For more information on setting up database connection.
