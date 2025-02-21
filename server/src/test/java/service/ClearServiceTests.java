package service;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import models.AuthData;
import models.GameData;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ClearServiceTests {
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;
    private static ClearService clearService;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        userDAO = new MemoryUserDAO();

        clearService = new ClearService(gameDAO, userDAO, authDAO);

        UserData testUser = new UserData("test", "test", "test");
        AuthData testAuth = new AuthData("authTest", "test");
        GameData testGame = new GameData(1234, "test", null,
                "testgame", new ChessGame());

        userDAO.createUser(testUser);
        authDAO.createAuth(testAuth);
        gameDAO.createGame(testGame);
    }

    @Test
    public void testClearService() {
        clearService.clear();

        Assertions.assertEquals(0, userDAO.size());
        Assertions.assertEquals(0, gameDAO.size());
        Assertions.assertEquals(0, authDAO.size());
    }
}
