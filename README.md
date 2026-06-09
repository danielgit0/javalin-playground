# Javalin + Flyway + JOOQ Demo

## Project Goal
A lightweight Javalin web service that demonstrates database migrations with Flyway and type‑safe data access with JOOQ. The application provides a simple User API (`/users`) backed by PostgreSQL.

## Architecture Overview
- **App.java** – Boots a Javalin server, configures routes, and wires the MVC components.
- **UserController** – HTTP layer exposing `GET /users` and `POST /users`.
- **UserService / UserServiceImpl** – Business logic, delegates to `UserRepository`.
- **UserRepository / UserRepositoryImpl** – Data‑access layer using JOOQ to insert and fetch `user` records.
- **AppFlyway** – Runs Flyway migrations on application start.
- **AppJooq** – Configures a JOOQ `DSLContext` tied to the Flyway‑migrated datasource.
- **PostgreSQL** – Stores the `user` table; the project includes a Docker/Podman Compose file for easy local setup.

## Prerequisites
- Java 11+
- Gradle wrapper (included)
- Docker/Podman (or a local PostgreSQL instance)

## Setup & Running

```bash
git clone https://github.com/your-org/javalin-playground.git
cd javalin-playground
podman compose up -d postgres      # starts the database
./gradlew clean build
java -jar build/libs/javalin-playground.jar
```

The service starts on `http://localhost:8081`.

## Database Migrations
Flyway runs on every application start (`AppFlyway.migrate()`). Migration scripts are located in `src/main/resources/db/migration`.

## Test Database
Integration tests use a separate database `javalin_test`. An initialization script (`config/db/init-test-db.sql`) creates this database, and Docker/Podman mounts it at container start. Tests reset the schema and seed data via `DatabaseTestSupport`.

## API Endpoints

| Method | Path        | Description |
|--------|-------------|-------------|
| `POST` | `/users`    | Creates a new user; returns its UUID and username. |
| `GET`  | `/users`    | Returns a list of all users. |
| `GET`  | `/health`   | Simple health‑check endpoint. |

## Testing

```bash
./gradlew test
```

Tests spin up the `javalin_test` database, apply migrations, and load seed users.

## Docker/Podman

```bash
podman compose up --build
```

The `compose.yml` starts both the app and a PostgreSQL container.

## License
MIT
