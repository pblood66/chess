package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.exceptions.DataAccessException;
import models.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.requests.CreateGameRequest;
import service.results.CreateGameResult;

public class GameServiceTests {
    private static GameDAO gameDAO;
    private static AuthDAO authDAO;
    private static GameService gameService;
    private static AuthData auth;

    @BeforeAll
    static void setUp() {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
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
    void createGamePositiveTest() throws DataAccessException {
        CreateGameRequest request = new CreateGameRequest(auth.authToken(), "Hello World");
        CreateGameResult result = gameService.createGame(request);

        Assertions.assertEquals(1, result.gameID());
        Assertions.assertEquals(1, gameDAO.size());
    }

    @Test
    void createGameNegativeTest() throws DataAccessException {
        CreateGameRequest request = new CreateGameRequest(auth.authToken(), "");
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(request));
    }



}
