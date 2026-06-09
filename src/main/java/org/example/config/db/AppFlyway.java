package org.example.config.db;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AppFlyway {

  private static final Logger logger = LoggerFactory.getLogger(AppFlyway.class);
  private static boolean migrated;

  static void migrate() {
    if (migrated) {
      return;
    }

    logger.info("Running Flyway migrations");
    Flyway.configure()
        .dataSource(
            AppDatabaseConfig.JDBC_URL,
            AppDatabaseConfig.JDBC_USER,
            AppDatabaseConfig.JDBC_PASSWORD)
        .locations("classpath:db/migration")
        .load()
        .migrate();
    migrated = true;
  }

  private AppFlyway() {}
}
