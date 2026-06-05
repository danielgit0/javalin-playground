package org.example.user.repository;

import java.util.List;

public interface UserRepository {

  User save(User user);

  List<User> findAll();
}
