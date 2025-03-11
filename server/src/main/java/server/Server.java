package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.mysql.MySqlGameDAO;
import dataaccess.mysql.MySqlUserDAO;
import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.UserDAO;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    private final ClearHandler clearHandler;
    private final UserHandler userHandler;
    private final GameHandler gameHandler;

    public Server() {

        AuthDAO authDAO = new MySqlAuthDAO();
        GameDAO gameDAO = new MySqlGameDAO();
        UserDAO userDAO = new MySqlUserDAO();

        ClearService clearService = new ClearService(gameDAO, userDAO, authDAO);
        clearHandler = new ClearHandler(clearService);

        UserService userService = new UserService(userDAO, authDAO);
        userHandler = new UserHandler(userService);

        GameService gameService = new GameService(gameDAO, authDAO);
        gameHandler = new GameHandler(gameService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::clear);

        Spark.post("/user", userHandler::register);
        Spark.post("/session", userHandler::login);
        Spark.delete("/session", userHandler::logout);

        Spark.post("/game", gameHandler::createGame);
        Spark.get("/game", gameHandler::listGames);
        Spark.put("/game", gameHandler::joinGame);



        // Exception Handling
        Spark.exception(DataAccessException.class, this::exceptionHandler);
        Spark.exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.body(new Gson().toJson(exception));
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

    private void exceptionHandler(DataAccessException exception, Request request, Response response) {
        response.status(exception.getStatusCode());
        response.body(exception.toJson());
    }
}
