# todoapp

creating todo
```
curl --location --request POST 'http://localhost:8080/todo/creation' \
--header 'accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
    "description": "desc",
    "status": "PAST_DUE",
    "dueDateTime": "2022-08-17T11:59:11.332Z",
    "expirationDateTime": "2022-11-17T11:59:11.332Z"
}'
```

updating todo
```
curl --location --request PUT 'http://localhost:8080/todos/1' \
--header 'accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
    "description": "desc2212121212",
    "status": "PAST_DUE",
    "id": 1
}'
```

retrieve a todo
```
curl --location --request GET 'http://localhost:8080/todos/1' \
--header 'accept: application/json' \
--header 'Content-Type: application/json'
```

retrieve all todos
```
curl --location --request GET 'http://localhost:8080/todos' \
--header 'accept: application/json' \
--header 'Content-Type: application/json'
```

retrieve not completed todos
```
curl --location --request GET 'http://localhost:8080/todos/notcompleted' \
--header 'accept: application/json' \
--header 'Content-Type: application/json'
```

### Tech Stack

---
- Java 11
- Spring Boot
- Spring Data JPA
- Restful API
- H2 In memory database
- Docker
- JUnit 5

### Prerequisites

---
- Maven

## Run & Build

first go to the terminal and open up the project directory. "~/todoapp/"

### Run the tests

mvn test

### Run project

mvn spring-boot:run

### Docker
to create the docker image execute the following commands
mvn clean package
mvn docker:build