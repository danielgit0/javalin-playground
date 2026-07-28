package org.example.user.service;

import java.util.List;
import java.util.UUID;

public interface UserService {

  UserDto createUser(CreateUserDto user);

  List<UserDto> getAllUsers();

  UserDto findById(UUID id);
}
