# 💻blog-api

🔨 Tecnologias

Esse projeto foi desenvolvido com as seguintes tecnologias:

> Spring Boot, Postgres, Spring Security, JWT, JPA, Rest API

## Instruções para o uso da API

### Auth

> POST: http://localhost:8080/api/auth/signUp -> Cadastra nova pessoa e seu(s) endereco(s)

> {
	"username": "caio98",
	"password": "password",
	"email": "caio@gmail.com"
 }

> DELETE: http://localhost:8080/pessoas/{id} -> Deleta uma pessoa. Necessario informar o {id} da pessoa!
