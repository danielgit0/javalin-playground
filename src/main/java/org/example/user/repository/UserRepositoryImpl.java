package org.example.user.repository;

import java.util.List;
import org.example.config.db.AppHibernate;

public class UserRepositoryImpl implements UserRepository {

  @Override
  public User save(User user) {
    AppHibernate.inTransaction(session -> session.insert(user));
    return user;
  }

  @Override
  public List<User> findAll() {
    return AppHibernate.fromTransaction(
        session -> session.createQuery("from User", User.class).getResultList());
  }
}
