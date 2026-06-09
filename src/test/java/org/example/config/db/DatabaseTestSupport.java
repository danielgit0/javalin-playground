package org.example.config.db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTestSupport {

  private static final String SEED_USERS_SCRIPT = "db/seed/seed_users.sql";

  public static void resetUsers() {
    // Ensure the test database schema is up‑to‑date before clearing data
    AppFlyway.migrate();
    try (Connection connection =
            DriverManager.getConnection(
                AppDatabaseConfig.JDBC_URL,
                AppDatabaseConfig.JDBC_USER,
                AppDatabaseConfig.JDBC_PASSWORD);
        Statement statement = connection.createStatement()) {
      // Remove any existing rows
      statement.executeUpdate("TRUNCATE TABLE \"user\"");
      // Load seed data for tests
      executeSqlScript(statement, SEED_USERS_SCRIPT);
    } catch (SQLException | IOException ex) {
      throw new IllegalStateException("Failed to reset test database", ex);
    }
  }

  private static void executeSqlScript(Statement statement, String classpathResource)
      throws IOException, SQLException {
    try (InputStream inputStream =
        DatabaseTestSupport.class.getClassLoader().getResourceAsStream(classpathResource)) {
      if (inputStream == null) {
        throw new IllegalStateException("SQL script not found on classpath: " + classpathResource);
      }
      var sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      for (String sqlStatement : sql.split(";")) {
        var trimmed = sqlStatement.trim();
        if (!trimmed.isEmpty()) {
          statement.executeUpdate(trimmed);
        }
      }
    }
  }

  private DatabaseTestSupport() {}
}
