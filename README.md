# Friend Hub

Friend Hub is a backend service for a social networking platform.
It provides a complete set of RESTful APIs to support core social features such as authentication, profile management, friend connections, group interactions, content posting, notifications, and real-time communication via WebSocket.

The project is built with Java and Spring Boot, following a layered architecture with a focus on clean structure and maintainability.

This project is intended for learning and local development purposes.
The database schema is automatically generated on application startup, and no pre-seeded data is required.

## Features

- JWT-based authentication and authorization
- User registration and profile management
- Friend request system (send, accept, reject, unfriend)
- Personal posts and group posts
- Like / Unlike posts
- Group creation and membership management
- Notifications
- Real-time chat and updates via WebSocket (STOMP)
- Reporting users and inappropriate content

## Tech Stack

- Java 17+
- Spring Boot 3.x
- MySQL
- Spring Data JPA & Hibernate
- Spring Security, JWT, OAuth2 Resource Server
- Spring WebSocket, STOMP
- Maven
- Lombok
- MapStruct
- Spring Boot Starter Validation

## Installation
Clone the repository:
```
https://github.com/vvnchuong/friend-hub.git
```

### Run with Docker (Recommended)
This is the easiest and recommended way to run the project. No need to install MySQL or Maven locally.

Configure application-docker.yml:
```
spring:
  datasource:
    url: jdbc:mysql://mysql:3306/YOUR_DATABASE
    username: USER_USERNAME
    password: YOUR_PASSWORD
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
  mail:
    host: smtp.gmail.com
    port: 587
    username: YOUR_EMAIL
    password: YOUR_APP_PASSWORD
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

Configure environment variables in docker-compose.yml:
```
MYSQL_ROOT_PASSWORD=YOUR_PASSWORD
MYSQL_DATABASE=YOUR_DATABASE
```

Start the application:
```
docker compose up --build -d
```

### Run Locally (Without Docker)
Use this approach if you want to run the application directly from your IDE.

Create database:
```
CREATE DATABASE YOUR_DATABASE;
```

Configure application.yml:
```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/YOUR_DATABASE
    username: USER_USERNAME
    password: YOUR_PASSWORD
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
  mail:
    host: smtp.gmail.com
    port: 587
    username: YOUR_EMAIL
    password: YOUR_APP_PASSWORD
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```
Build the project:
```
./mvnw clean install
```
or
```
mvn clean install
```

Run the application:
```
./mvnw spring-boot:run
```
or run FriendHubApplication.java directly from your IDE.

## Configuration Profiles
- application.yml is used for local development.
- application-docker.yml is used when running inside Docker.
- Docker activates the docker profile automatically via: SPRING_PROFILES_ACTIVE=docker

## API Testing
- All APIs are provided via a Postman collection.
- Postman file: [file](./postman/FriendHub.json).
- Authentication: JWT Bearer Token

## Frontend (Bonus)
- This is a simple frontend built with React, used to test the APIs instead of Postman.
- Clone this repository:
```
https://github.com/vvnchuong/friend-hub-fe.git
```
- Run the application:
```
cd friend-hub-fe
npm install
npm run dev
```
