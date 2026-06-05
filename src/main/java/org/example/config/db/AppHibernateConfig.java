package org.example.config.db;

import java.util.Properties;
import org.example.user.repository.User;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;

class AppHibernateConfig {

  static Configuration configuration() {
    var configuration = new Configuration();
    var settings = new Properties();
    settings.put(AvailableSettings.JAKARTA_JDBC_DRIVER, "org.postgresql.Driver");
    settings.put(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:postgresql://localhost:5432/javalin");
    settings.put(AvailableSettings.JAKARTA_JDBC_USER, "postgres");
    settings.put(AvailableSettings.JAKARTA_JDBC_PASSWORD, "postgres");
    settings.put(AvailableSettings.HIGHLIGHT_SQL, true);
    settings.put(AvailableSettings.HBM2DDL_AUTO, Action.ACTION_CREATE);
    settings.put(AvailableSettings.GLOBALLY_QUOTED_IDENTIFIERS, "true");
    settings.put("jakarta.persistence.schema-generation.database.action", "drop-and-create");
    settings.put("jakarta.persistence.sql-load-script-source", "import.sql");

    configuration.setProperties(settings);
    configuration.addAnnotatedClass(User.class);
    return configuration;
  }
}
