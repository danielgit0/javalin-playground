package org.example;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

import io.javalin.Javalin;
import org.example.user.controller.UserController;
import org.example.user.repository.UserRepository;
import org.example.user.repository.UserRepositoryImpl;
import org.example.user.service.UserService;
import org.example.user.service.UserServiceImpl;

public class App {

  private final Javalin app;

  public App() {
    UserRepository userRepository = new UserRepositoryImpl();
    UserService userService = new UserServiceImpl(userRepository);
    UserController userController = new UserController(userService);

    this.app =
        Javalin.create(
            config -> {
              config.routes.apiBuilder(
                  () -> {
                    path(
                        "/users",
                        () -> {
                          get(userController::getAll);
                          post(userController::create);
                        });

                    get("/ui", ctx -> ctx.html("<h1>User UI</h1>"));
                  });
            });
  }

  static void main(String[] args) {
    var application = new App();
    application.javalinApp().start(8081);
  }

  public Javalin javalinApp() {
    return this.app;
  }
}
