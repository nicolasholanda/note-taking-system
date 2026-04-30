# Note Taking System

A simple web app to write, organize, and keep track of your notes. Built with Spring Boot and Thymeleaf.

## Tech stack

- Java 21
- Spring Boot 3.4
- Thymeleaf + Bootstrap 5
- Spring Data JPA
- H2 (file-based, data persists between restarts)

## Features

- Create, edit, and delete notes
- Search notes by title
- Auto-save while editing — changes sync to the server as you type

## Running locally

Make sure you have Java 21 and Maven installed, then:

```bash
./mvnw spring-boot:run
```

The app will be available at `http://localhost:8080/notes`.

H2 console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/notesdb`, no password).
