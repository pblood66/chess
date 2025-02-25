package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UnauthoriedException;
import dataaccess.UserDAO;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DuplicatedException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;

    private final ClearHandler clearHandler;
    private final UserHandler userHandler;

    public Server() {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        userDAO = new MemoryUserDAO();

        clearService = new ClearService(gameDAO, userDAO, authDAO);
        clearHandler = new ClearHandler(clearService);

        userService = new UserService(userDAO, authDAO);
        userHandler = new UserHandler(userService);

        gameService = new GameService(gameDAO, authDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::clear);
        Spark.post("/user", userHandler::register);
        Spark.post("/session", userHandler::login);
        Spark.delete("/session", userHandler::logout);


        // Exception Handling
        Spark.exception(BadRequestException.class,
                (exception, request, response) -> {
            response.status(400);
            response.body(new Gson().toJson(exception.getMessage()));
                });
        Spark.exception(DuplicatedException.class,
                (exception, request, response) -> {
            response.status(403);
            response.body(new Gson().toJson(exception.getMessage()));
                });
        Spark.exception(UnauthoriedException.class,
                (exception, request, response) -> {
            response.status(401);
            response.body(exception.getMessage());
                });
        Spark.exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.body(new Gson().toJson(exception.getMessage()));
                });

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
