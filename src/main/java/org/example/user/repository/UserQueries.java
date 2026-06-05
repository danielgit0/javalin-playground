package org.example.user.repository;

import java.util.List;
import org.hibernate.StatelessSession;
import org.hibernate.annotations.processing.Find;

public interface UserQueries {

  @Find
  List<User> getAllUsers(StatelessSession session);
}
