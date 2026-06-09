package org.example.user.repository;

import java.util.UUID;

public class User {

  private UUID id;
  private String username;

  public User() {}

  public User(String username) {
    this.username = username;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
