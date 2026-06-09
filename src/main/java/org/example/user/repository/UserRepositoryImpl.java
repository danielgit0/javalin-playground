package org.example.user.repository;

import static org.example.jooq.generated.tables.User.USER;

import java.util.List;
import java.util.UUID;
import org.example.config.db.AppJooq;
import org.example.jooq.generated.tables.records.UserRecord;

public class UserRepositoryImpl implements UserRepository {

  @Override
  public User save(User user) {
    if (user.getId() == null) {
      user.setId(UUID.randomUUID());
    }

    AppJooq.dsl()
        .insertInto(USER)
        .set(USER.ID, user.getId())
        .set(USER.USERNAME, user.getUsername())
        .execute();
    return user;
  }

  @Override
  public List<User> findAll() {
    return AppJooq.dsl().selectFrom(USER).fetch(this::toUser);
  }

  private User toUser(UserRecord record) {
    var user = new User();
    user.setId(record.getId());
    user.setUsername(record.getUsername());
    return user;
  }
}
