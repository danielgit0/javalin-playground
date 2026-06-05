package org.example.user.service;

import java.util.List;

public interface UserService {

  UserDto createUser(CreateUserDto user);

  List<UserDto> getAllUsers();
}
