package dataaccess;

import chess.ChessGame;
import dataaccess.MySQL.MySqlGameDAO;
import dataaccess.memory.MemoryGameDAO;
import models.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class GameDAOTests {
    private GameDAO getDataAccess(Class<? extends GameDAO> databaseClass) {
        GameDAO db;

        if (databaseClass == MySqlGameDAO.class) {
            db = new MySqlGameDAO();
        } else {
            db = new MemoryGameDAO();
        }

        db.clear();
        return db;
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlGameDAO.class, MemoryGameDAO.class})
    void createGamePositiveTest(Class<? extends GameDAO> databaseClass) {
        GameDAO db = getDataAccess(databaseClass);

        GameData game = new GameData(1, null,
                null, "testGame", new ChessGame());

        Assertions.assertDoesNotThrow(() -> db.createGame(game));
        Assertions.assertEquals(1, db.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlGameDAO.class, MemoryGameDAO.class})
    void createGameNegativeTest(Class<? extends GameDAO> databaseClass) {
        GameDAO db = getDataAccess(databaseClass);

    }


}
