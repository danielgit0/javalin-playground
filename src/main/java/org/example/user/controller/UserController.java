package org.example.user.controller;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.util.List;
import java.util.UUID;
import org.example.user.service.CreateUserDto;
import org.example.user.service.UserDto;
import org.example.user.service.UserService;

public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  public void create(Context ctx) {
    CreateUserRequest request = ctx.bodyAsClass(CreateUserRequest.class);
    if (request == null || request.username().length() < 5) {
      throw new BadRequestResponse("Username must be at least 5 characters");
    }

    UserDto user = userService.createUser(new CreateUserDto(request.username()));

    if (user == null) {
      ctx.status(HttpStatus.NOT_FOUND);
    } else {
      ctx.json(new UserResponse(user.id(), user.username())).status(HttpStatus.CREATED);
    }
  }

  public void getAll(Context ctx) {
    List<UserResponse> users =
        userService.getAllUsers().stream()
            .map(u -> new UserResponse(u.id(), u.username()))
            .toList();

    ctx.json(new UsersResponse(users)).status(HttpStatus.OK);
  }

  public void findById(Context ctx) {
    final String idStr = ctx.pathParam("id");
    final UUID id;
    try {
      id = UUID.fromString(idStr);
    } catch (IllegalArgumentException e) {
      throw new BadRequestResponse("Invalid UUID string: " + idStr);
    }

    UserDto user = userService.findById(id);

    if (user == null) {
      ctx.status(HttpStatus.NOT_FOUND);
    } else {
      ctx.json(new UserResponse(user.id(), user.username())).status(HttpStatus.CREATED);
    }
  }
}
