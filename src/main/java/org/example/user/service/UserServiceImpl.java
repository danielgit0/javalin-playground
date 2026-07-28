package org.example.user.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.example.user.repository.User;
import org.example.user.repository.UserRepository;

public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDto createUser(CreateUserDto user) {
    User newUser = new User(user.username());
    Optional<User> savedUserOptional = userRepository.save(newUser);
    if (savedUserOptional.isEmpty()) {
      return null;
    }

    final User savedUser = savedUserOptional.get();
    return new UserDto(savedUser.getId(), savedUser.getUsername());
  }

  @Override
  public List<UserDto> getAllUsers() {
    return userRepository.findAll().stream()
        .map(user -> new UserDto(user.getId(), user.getUsername()))
        .collect(Collectors.toList());
  }

  @Override
  public UserDto findById(UUID id) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isEmpty()) {
      return null;
    }
    final User user = userOptional.get();
    return new UserDto(user.getId(), user.getUsername());
  }
}
