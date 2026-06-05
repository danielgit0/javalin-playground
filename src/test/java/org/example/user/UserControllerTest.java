package org.example.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.example.user.controller.CreateUserRequest;
import org.example.user.controller.UserController;
import org.example.user.controller.UserResponse;
import org.example.user.repository.UserRepository;
import org.example.user.service.UserService;
import org.example.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;

public class UserControllerTest {

  private Context ctx;
  private UserController userController;

  @BeforeEach
  void setUp() {
    ctx = mock(Context.class, Answers.RETURNS_SELF);
    UserRepository userRepository = new FakeUserRepository();
    UserService userService = new UserServiceImpl(userRepository);
    userController = new UserController(userService);
  }

  @Test
  public void POST_to_create_users_gives_201_for_valid_username() {
    CreateUserRequest requestDto = new CreateUserRequest("Roland");
    when(ctx.bodyAsClass(CreateUserRequest.class)).thenReturn(requestDto);

    ArgumentCaptor<UserResponse> responseCaptor = ArgumentCaptor.forClass(UserResponse.class);

    userController.create(ctx);

    verify(ctx).status(HttpStatus.CREATED);

    verify(ctx).json(responseCaptor.capture());
    UserResponse actualResponse = responseCaptor.getValue();

    assertThat(actualResponse).isNotNull();
    assertThat(actualResponse.id()).isNotNull();
    assertThat(actualResponse.username()).isEqualTo("Roland");
  }

  @Test
  public void POST_to_create_users_throws_for_invalid_username() {
    CreateUserRequest shortUserRequest = new CreateUserRequest("Bob");
    when(ctx.bodyAsClass(CreateUserRequest.class)).thenReturn(shortUserRequest);

    BadRequestResponse exception =
        assertThrows(
            BadRequestResponse.class,
            () -> {
              userController.create(ctx);
            });

    assertEquals("Username must be at least 5 characters", exception.getMessage());
  }
}
