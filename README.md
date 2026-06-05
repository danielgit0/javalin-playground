# Javalin Transactions Demo with Postgres

## Project Goal
A lightweight Javalin web service that demonstrates how to manage database transactions using Hibernate's `StatelessSession` with a PostgreSQL backend. The application provides a simple User API (`/users`) that creates and retrieves user records while explicitly handling transaction commit/rollback.

## Architecture Overview
- **App.java** – Boots a Javalin server, configures routes, and wires the MVC components.
- **UserController** – HTTP layer exposing `GET /users` and `POST /users`.
- **UserService / UserServiceImpl** – Business logic, delegates to `UserRepository`.
- **UserRepository / UserRepositoryImpl** – Data‑access layer using `AppHibernate.inTransaction` and `AppHibernate.fromTransaction` to execute Hibernate operations within a transaction.
- **AppHibernate** – Utility class wrapping a `StatelessSessionFactory` to run code in a transaction without the overhead of a full Hibernate session.
- **PostgreSQL** – Stores the `users` table; the project includes a Docker Compose file for easy local setup.

## Prerequisites
- Java 11+
- Maven (or Gradle wrapper)
- PostgreSQL 12+ (or Docker)

## Setup & Running
```bash
git clone https://github.com/your-org/javalin-playground.git
cd javalin-playground
# Start PostgreSQL (optional if you have it locally)
docker compose up -d db
# Build and run
./gradlew clean build
java -jar build/libs/javalin-playground.jar
```
The service starts on `http://localhost:7000`.

## API Endpoints
| Method | Path        | Description |
|--------|-------------|-------------|
| `POST` | `/users`    | Creates a new user; transaction is committed on success or rolled back on error. |
| `GET`  | `/users`    | Returns a list of all users. |
| `GET`  | `/health`   | Simple health‑check endpoint. |

## Testing
```bash
./gradlew test
```
Integration tests spin up an in‑memory PostgreSQL instance and verify transactional behavior.

## Docker
```bash
docker compose up --build
```
The `docker-compose.yml` starts both the app and a PostgreSQL container.

## License
MIT
