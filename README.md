# Agile Project Manager API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)


A robust, modular REST API for managing agile projects, designed with a focus on **Software Engineering best practices**, **Clean Architecture**, and **scalability**.

This project serves as a technical showcase for advanced Java Spring Boot development, demonstrating a clear separation of concerns, domain-driven design principles, and readiness for microservices evolution.

## 🏗 Project Structure
### The Core Philosophy: "Domain First"
Unlike traditional N-Tier architectures where the database dictates the model, this project places the **Domain** at the center.

1.  **Domain Layer (`domain`):**
    * Contains pure Java objects (`Models`) representing business entities (e.g., `Sprint`, `UserStory`).
    * Contains **Business Rules** and **Exceptions**.
    * **Crucially:** It has **ZERO dependencies** on frameworks (Spring, Hibernate) or infrastructure. It is pure Java logic.

2.  **Application Layer (`application`):**
    * Orchestrates business flows using **Use Cases** (e.g., `StartSprintUseCase`, `CreateProjectUseCase`) rather than generic "Services".
    * Implements the **Single Responsibility Principle (SRP)**.

3.  **Infrastructure Layer (`infrastructure`):**
    * Handles technical concerns: Database persistence, Security (JWT), and external libraries.
    * **Persistence Decoupling:** We use **MapStruct** to map between `Domain Models` (Business) and `JPA Entities` (Persistence). This ensures that changes in the database schema do not pollute the core business logic.

4.  **Interface Layer (`api`):**
    * The entry point (REST Controllers).
    * Handles `DTO` conversions to protect the inner layers from external contract changes.

The project follows a strict layered architecture:

```
src/main/java/com/arturcapelossi/agilepm
│
├── api              # REST Controllers & DTOs (Entry Point)
├── application      # Use Cases (Business Actions) & Application Services
├── domain               # Pure Business Logic (No Spring/JPA here)
│   ├── model            # Rich Domain Models
│   ├── repository       # Repository Interfaces
│   └── exception        # Domain Exceptions
├── infrastructure       # Implementation details
│   ├── persistence      # JPA Entities & Repositories
│   ├── security         # JWT & Auth config
│   └── config           # App configuration
├── config           # Spring Configuration (OpenAPI, etc.)
└── common           # Shared Utilities
```

## 🚀 Key Technical Highlights

*   **Modular Monolith Architecture:** Structured to be easily decomposed into microservices. Follows Clean Architecture principles with distinct layers: `API`, `Application`, `Domain`, and `Infrastructure`.
*   **Domain-Driven Design (DDD) Influence:** Core business logic is isolated in the `Domain` layer, independent of frameworks.
*   **Explicit Decoupling:** Utilizes **MapStruct** with advanced `CycleAvoidingMappingContext` to strictly separate JPA Entities from Domain Models and DTOs, preventing "anemic domain model" anti-patterns and ensuring data integrity.
*   **Use Case Pattern:** Business logic is encapsulated in granular Use Cases (e.g., `StartSprintUseCase`, `AddUserStoryToSprintUseCase`) rather than bloated Services, improving readability and testability.
*   **Robust Security:** Stateless authentication using **Spring Security** and **JWT** (JSON Web Tokens) with role-based access control.
*   **Database Versioning:** Automated schema migrations using **Flyway** ensure consistent database states across environments.
*   **Containerization:** Fully Dockerized application and database using **Docker Compose** for effortless deployment.
*   **API Documentation:** Comprehensive **OpenAPI (Swagger)** documentation for all endpoints.

## ✨ Functional Features

*   **User Management:** Secure registration and login with JWT.
*   **Project Management:** Create projects, update details, and manage team members.
*   **Sprint Management:** Plan sprints, start them, and close them. Enforces rules like "only one active sprint per project".
*   **User Stories:** Manage backlog, assign stories to sprints, and track status (Todo, In Progress, Done).
*   **Task Management:** Break down stories into tasks, assign to members, and track progress.
*   **Collaboration:** Comment on tasks to facilitate team communication.

## 🛠 Tech Stack

*   **Language:** Java 21
*   **Framework:** Spring Boot 3.2.5
*   **Database:** PostgreSQL 15
*   **ORM/Persistence:** Spring Data JPA, Hibernate
*   **Migrations:** Flyway
*   **Object Mapping:** MapStruct
*   **Security:** Spring Security, JJWT
*   **Documentation:** SpringDoc OpenAPI (Swagger UI)
*   **DevOps:** Docker, Docker Compose

## ⚙️ Getting Started

### Prerequisites
*   Docker & Docker Compose
*   (Optional) Java 21 SDK & Maven if running locally without Docker

### 🐳 Running with Docker (Recommended)

The easiest way to run the API and the Database is via Docker Compose.

1.  **Build and Start:**
    ```bash
    docker-compose up -d --build
    ```

2.  **Access the API:**
    The application will start on port `8080`.

### 🏃‍♂️ Running Locally
If you prefer to run it in your IDE:

1. Ensure you have a PostgreSQL instance running (or update `application.yml` to use H2).
2. Configure environment variables in a `.env` file (see `.envtemplate`):

```properties
POSTGRES_DB=agile_project_manager
POSTGRES_USER=postgres
POSTGRES_PASSWORD=password
JWT_SECRET=YOUR_SECURE_SECRET_KEY
```

3. Run the application:

```bash
./mvnw spring-boot:run
```


## 📚 API Documentation

Once the application is running, you can explore and test the API endpoints using the interactive Swagger UI:

👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

## 🔮 Roadmap

This project is designed as a foundation for further evolution:

- [ ] **Testing:** Comprehensive Unit and Integration tests (JUnit 5, Mockito, Testcontainers).
- [ ] **Event-Driven Architecture:** Introduce RabbitMQ/Kafka for asynchronous communication (e.g., notifications).
- [ ] **Microservices Extraction:** Refactor distinct modules (e.g., Identity, Project Management) into separate services.
- [ ] **CI/CD:** GitHub Actions pipelines for automated testing and deployment.

## 👨‍💻 Author

**Artur Capelossi**
*Final-year Software Engineering Student @ ISEC*

Developed to demonstrate maturity in Software Engineering, Architecture, and Java Ecosystems.

