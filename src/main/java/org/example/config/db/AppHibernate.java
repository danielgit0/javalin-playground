package org.example.config.db;

import java.util.function.Consumer;
import java.util.function.Function;
import org.hibernate.StatelessSession;

public class AppHibernate {

  public static void inTransaction(Consumer<StatelessSession> consumer) {
    AppHibernateSessionFactory.getSessionFactory().inStatelessTransaction(consumer);
  }

  public static <R> R fromTransaction(Function<StatelessSession, R> function) {
    return AppHibernateSessionFactory.getSessionFactory().fromStatelessTransaction(function);
  }
}
