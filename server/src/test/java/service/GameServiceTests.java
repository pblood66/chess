package service;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.MySQL.MySqlAuthDAO;
import dataaccess.MySQL.MySqlGameDAO;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.DuplicatedException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import models.AuthData;
import models.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.requests.ListGamesRequest;
import service.results.CreateGameResult;
import service.results.ListGamesResult;

public class GameServiceTests {
    private static GameDAO gameDAO;
    private static AuthDAO authDAO;
    private static GameService gameService;
    private static AuthData auth;

    @BeforeAll
    static void setUp() {
        gameDAO = new MySqlGameDAO();
        gameDAO.clear();
        authDAO = new MySqlAuthDAO();
        authDAO.clear();
        gameService = new GameService(gameDAO, authDAO);

        auth = new AuthData("token", "pblood66");

        try {
            authDAO.createAuth(auth);
        }
        catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @Order(1)
    void createGamePositiveTest() throws DataAccessException {
        CreateGameRequest request = new CreateGameRequest(auth.authToken(), "Hello World");
        CreateGameResult result = gameService.createGame(request);

        Assertions.assertEquals(1, result.gameID());
        Assertions.assertEquals(1, gameDAO.size());
    }

    @Test
    @Order(2)
    void createGameNegativeTest() throws DataAccessException {
        CreateGameRequest request = new CreateGameRequest(auth.authToken(), "");
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(request));
    }

    @Test
    @Order(3)
    void joinGamePositiveTest() throws DataAccessException {
        JoinGameRequest request = new JoinGameRequest(auth.authToken(), "WHITE", 1);
        gameService.joinGame(request);

        GameData game = gameDAO.getGame(1);

        Assertions.assertEquals("pblood66", game.whiteUsername());
    }

    @Test
    @Order(4)
    void joinGameNegativeTest() throws DataAccessException {
        JoinGameRequest request = new JoinGameRequest(auth.authToken(), "WHITE", 1);

        Assertions.assertThrows(DuplicatedException.class, () -> gameService.joinGame(request));
    }

    @Test
    @Order(5)
    void listGamesPositiveTest() throws DataAccessException {
        CreateGameRequest request = new CreateGameRequest(auth.authToken(), "test1");
        gameService.createGame(request);
        request = new CreateGameRequest(auth.authToken(), "test2");
        gameService.createGame(request);

        ListGamesRequest list = new ListGamesRequest(auth.authToken());

        ListGamesResult result = gameService.listGames(list);

        Assertions.assertEquals(3, result.games().size());
    }

    @Test
    @Order(6)
    void listGamesNegativeTest() throws DataAccessException {
        ListGamesRequest request = new ListGamesRequest("error");

        Assertions.assertThrows(DataAccessException.class, () -> gameService.listGames(request));
    }



}
