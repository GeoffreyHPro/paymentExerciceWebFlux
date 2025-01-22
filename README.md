# Project of payment API

The project is a new version of paymentExercice : https://github.com/GeoffreyHPro/paymentExercice
the old version works with h2 database and jpa.
This new version works with postgresql and webflux. 

This project allows to create, read, and modify a payment. 

Here the database in API

<img src="public/demo.png" width="350"/>

# 👩‍💻 Technologies

| Back - Spring Boot | version (works) | 
| --- | --- |
| java | 22 |
| maven| 3.9.6 |
| Docker| 26.1.1 |

# 🛠 Project and funtionnalities

✅ Read a command

✅ Read a payment

⬜ create a command

⬜ upload a command

⬜ create a payment

⬜ upload a payment

# ⚡️ Standard Execution

Start postgresql database

```bash
docker-compose up
```

Start API REST

```bash
mvn spring-boot:run
```

URL of Swagger: http://localhost:8080/swagger-ui.html

# Future functionalities and perspective

Idea for future database

<img src="public/futur.png" width="350"/>