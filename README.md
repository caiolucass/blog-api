# 💻blog-api

1. Clonar a aplicação:

> git clone https://github.com/caiolucass/blog-api

### Auth

> POST: http://localhost:8080/api/auth/signUp

```json
{
	"username": "caio98",
	"password": "password",
	"email": "caio@gmail.com"
}

```
> POST: http://localhost:8080/api/auth/signIn

```json
{
	"username": "caio98",
	"password": "password",
	"email": "caio@gmail.com"
}

```
### Usuários

> GET: http://localhost:8080/api/users/me

> GET: http://localhost:8080/api/users/{username}/profile

> GET: http://localhost:8080/api/users/{username}/posts

> GET: http://localhost:8080/api/users/{username}/albums

> GET: http://localhost:8080/api/users/checkUsernameAvailability

> GET: http://localhost:8080/api/users/checkEmailAvailability

> POST: http://localhost:8080/api/users

> POST: http://localhost:8080/api/users/{username}

> DELETE: http://localhost:8080/api/users/{username}

> PUT: http://localhost:8080/api/users/{username}/giveAdmin

> PUT: http://localhost:8080/api/users/{username}/TakeAdmin

> PUT: http://localhost:8080/api/users/setOrUpdateInfo


