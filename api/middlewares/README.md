# Middleware

All custom express middleware are found here.

## auth.js
### Description
Handle app authentication by verifying *Authorization* header.

Default format of *Authorization* header is `JWT <token>`

1. Read *Authorization* header
2. Decode with `JWT_SECRET`
3. Query *user* by decoded id
4. Attach *user* to `req.user`

### Config

*AUTHORIZATION_TYPE* - *Authorization* header prefix, i.e. `Bearer`, `JWT`
