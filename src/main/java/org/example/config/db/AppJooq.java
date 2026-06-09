package org.example.config.db;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.postgresql.ds.PGSimpleDataSource;

public class AppJooq {

  private static DSLContext dslContext;

  public static DSLContext dsl() {
    if (dslContext == null) {
      AppFlyway.migrate();

      var dataSource = new PGSimpleDataSource();
      dataSource.setURL(AppDatabaseConfig.JDBC_URL);
      dataSource.setUser(AppDatabaseConfig.JDBC_USER);
      dataSource.setPassword(AppDatabaseConfig.JDBC_PASSWORD);

      dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
    }
    return dslContext;
  }

  private AppJooq() {}
}
