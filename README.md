<h1 align="center">Blog API</h1>

## Clonar a aplicação:

`git clone https://github.com/caiolucass/blog-api`

## Tecnologias utilizadas:

- [Spring Boot](https://spring.io/) - [Postgres](https://www.postgresql.org/) - [Spring Security](https://spring.io/projects/spring-security) - [JSONWebToken](https://jwt.io/) - [JPA](https://spring.io/projects/spring-data-jpa) - [MAVEN](https://maven.apache.org/)

- Para rodar o projeto é necessário utilizar os postgres e utilizar as credenciais do arquivo `application.yml`

## Json requests:

> `POST: http://localhost:8080/api/auth/signIn`

```json
{
	"username": "caio98",
	"password": "password",
	"email": "caio@gmail.com"
}

```

> `POST: http://localhost:8080/api/auth/signUp` 

```json
{
	"username": "caio98",
	"password": "password",
	"email": "caio@gmail.com"
}

```

```json
{
	"username": "caio98",
	"password": "password",
	"email": "caio@gmail.com"
}

```

## Endpoints da aplicação:

### Usuários

> GET: `http://localhost:8080/api/users/me`

> GET: `http://localhost:8080/api/users/{username}/profile`

> GET: `http://localhost:8080/api/users/{username}/posts`

> GET: `http://localhost:8080/api/users/{username}/albums`

> GET: `http://localhost:8080/api/users/checkUsernameAvailability`

> GET: `http://localhost:8080/api/users/checkEmailAvailability`

> POST: `http://localhost:8080/api/users`

> POST: `http://localhost:8080/api/users/{username}`

> DELETE: `http://localhost:8080/api/users/{username}`

> PUT: `http://localhost:8080/api/users/{username}/giveAdmin`

> PUT: `http://localhost:8080/api/users/{username}/TakeAdmin`

> PUT: `http://localhost:8080/api/users/setOrUpdateInfo`

### Post

> GET: `http://localhost:8080/api/posts`

> GET: `http://localhost:8080/api/posts/{id}`

> POST: `http://localhost:8080/api/posts`

> PUT: `http://localhost:8080/api/posts/{id}`

> DELTE: `http://localhost:8080/api/posts/{id}`

### Comments

> GET: `http://localhost:8080/api/posts/{postId}/comments`

> GET: `http://localhost:8080/api/posts/{postId}/comments/{id}`

> POST: `http://localhost:8080/api/posts/{postId}/comments`

> PUT: `http://localhost:8080/api/posts/{postId}/comments/{id}`

> DELETE: `http://localhost:8080/api/posts/{postId}/comments/{id}`

### Albuns

> GET: `http://localhost:8080/api/albums`

> GET: `http://localhost:8080/api/albums/{id}`

> POST: `http://localhost:8080/api/albums}`

> PUT: `http://localhost:8080/api/albums/{id}`

> DELETE: `http://localhost:8080/api/albums/{id}`

> GET: `http://localhost:8080/api/albums/{id}/photos`

### PHOTOS

> GET: `http://localhost:8080/api/photos`

> GET: `http://localhost:8080/api/photos/{id}`

> POST: `http://localhost:8080/api/photos`

> PUT: `http://localhost:8080/api/photos/{id}`

> DELETE: `http://localhost:8080/api/photos/{id}`

Para testar so endpoints, é necessario utilizar o [Postman](https://www.postman.com/) ou o [Insomnia](https://insomnia.rest/download)

