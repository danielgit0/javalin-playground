package org.example.config.db;

class AppDatabaseConfig {

  static final String JDBC_URL =
      System.getenv().getOrDefault("JDBC_URL", "jdbc:postgresql://localhost:5432/javalin");
  static final String JDBC_USER = System.getenv().getOrDefault("JDBC_USER", "postgres");
  static final String JDBC_PASSWORD = System.getenv().getOrDefault("JDBC_PASSWORD", "postgres");

  private AppDatabaseConfig() {}
}
