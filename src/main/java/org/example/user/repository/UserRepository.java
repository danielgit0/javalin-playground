package org.example.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  Optional<User> save(User user);

  List<User> findAll();

  Optional<User> findById(UUID id);
}
