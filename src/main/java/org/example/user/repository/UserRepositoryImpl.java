package org.example.user.repository;

import static org.example.jooq.generated.tables.User.USER;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.example.config.db.AppJooq;
import org.example.jooq.generated.tables.records.UserRecord;

public class UserRepositoryImpl implements UserRepository {

  @Override
  public Optional<User> save(User user) {
    if (user.getId() == null) {
      user.setId(UUID.randomUUID());
    }

    AppJooq.dsl()
        .insertInto(USER)
        .set(USER.ID, user.getId())
        .set(USER.USERNAME, user.getUsername())
        .execute();
    return findById(user.getId());
  }

  @Override
  public List<User> findAll() {
    return AppJooq.dsl().selectFrom(USER).fetch(this::toUser);
  }

  @Override
  public Optional<User> findById(UUID id) {
    return AppJooq.dsl().selectFrom(USER).where(USER.ID.eq(id)).fetchOptionalInto(User.class);
  }

  private User toUser(UserRecord record) {
    var user = new User();
    user.setId(record.getId());
    user.setUsername(record.getUsername());
    return user;
  }
}
