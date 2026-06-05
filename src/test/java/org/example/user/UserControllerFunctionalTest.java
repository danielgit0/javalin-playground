package org.example.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import io.javalin.testtools.JavalinTest;
import org.example.App;
import org.example.user.controller.UserResponse;
import org.example.user.controller.UsersResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserControllerFunctionalTest {

  Javalin app = new App().javalinApp();
  private JavalinJackson javalinJackson;

  @BeforeEach
  void setUp() {
    app = new App().javalinApp();
    javalinJackson = new JavalinJackson();
  }

  @Test
  public void GET_to_fetch_users_returns_list_of_users() {
    JavalinTest.test(
        app,
        (server, client) -> {
          var response = client.get("/users");

          assertThat(response.code()).isEqualTo(200);

          UsersResponse usersResponse =
              javalinJackson.fromJsonString(
                  response.body().string(), new TypeReference<UsersResponse>() {}.getType());

          assertThat(usersResponse.users())
              .hasSize(3)
              // .extracting(UserResponse::id, UserResponse::username)
              .extracting(UserResponse::username)
              .containsExactly("User1", "User2", "User3");
        });
  }

  @Test
  public void POST_to_create_user_returns_correct_json_structure() {
    JavalinTest.test(
        app,
        (server, client) -> {
          String requestJson =
              """
          {
            "username": "Roland"
          }
          """;

          var response = client.post("/users", requestJson);
          assertThat(response.code()).isEqualTo(201);

          UserResponse userResponse =
              javalinJackson.fromJsonString(
                  response.body().string(), new TypeReference<UserResponse>() {}.getType());

          assertThat(userResponse.id().toString()).matches("[a-f0-9-]+");
          assertThat(userResponse.username()).isEqualTo("Roland");
        });
  }
}
