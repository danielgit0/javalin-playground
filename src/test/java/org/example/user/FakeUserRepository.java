package org.example.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.example.user.repository.User;
import org.example.user.repository.UserRepository;

public class FakeUserRepository implements UserRepository {

  private final Map<UUID, User> databaseSimulator = new ConcurrentHashMap<>();

  @Override
  public User save(User user) {
    if (user.getId() == null) {
      user.setId(UUID.randomUUID());
    }
    databaseSimulator.put(user.getId(), user);
    return user;
  }

  @Override
  public List<User> findAll() {
    return new ArrayList<>(databaseSimulator.values());
  }
}
