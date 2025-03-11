package dataaccess;

import chess.ChessGame;
import dataaccess.MySQL.MySqlGameDAO;
import dataaccess.exceptions.DataAccessException;
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

        GameData game = new GameData(
                1,
                null,
                null,
                "testGame",
                new ChessGame());

        Assertions.assertDoesNotThrow(() -> db.createGame(game));
        Assertions.assertEquals(1, db.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlGameDAO.class})
    void createGameNegativeTest(Class<? extends GameDAO> databaseClass) {
        GameDAO db = getDataAccess(databaseClass);

        GameData game = new GameData(
                0,
                null,
                null,
                null,
                null);

        Assertions.assertThrows(DataAccessException.class, () -> db.createGame(game));

    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlGameDAO.class, MemoryGameDAO.class})
    void updateGamePositiveTest(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        GameDAO db = getDataAccess(databaseClass);

        GameData game = new GameData(
                1,
                null,
                null,
                "testGame",
                new ChessGame());

        db.createGame(game);

        GameData updatedGame = new GameData(
                1,
                "User",
                "",
                "testGame",
                new ChessGame());

        Assertions.assertDoesNotThrow(() -> db.updateGame(updatedGame));
        Assertions.assertEquals(1, db.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlGameDAO.class, MemoryGameDAO.class})
    void updateGameNegativeTest(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        GameDAO db = getDataAccess(databaseClass);

        GameData update = new GameData(
                1,
                "Hello",
                "",
                "testGame",
                new ChessGame()
        );

        Assertions.assertThrows(DataAccessException.class, () -> db.updateGame(update));

    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlGameDAO.class, MemoryGameDAO.class})
    void getGamePositiveTest(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        GameDAO db = getDataAccess(databaseClass);
        GameData game = new GameData(1,
                "Hello",
                "",
                "testGame",
                new ChessGame());

        db.createGame(game);

        GameData result = db.getGame(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(game.gameID(), result.gameID());
        Assertions.assertEquals(game.gameName(), result.gameName());
        Assertions.assertEquals(game.whiteUsername(), result.whiteUsername());
        Assertions.assertEquals(game.blackUsername(), result.blackUsername());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlGameDAO.class, MemoryGameDAO.class})
    void getGameNegativeTest(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        GameDAO db = getDataAccess(databaseClass);

        Assertions.assertThrows(DataAccessException.class, () -> db.getGame(0));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlGameDAO.class, MemoryGameDAO.class})
    void clearGamePositiveTest(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        GameDAO db = getDataAccess(databaseClass);
        GameData game = new GameData(
                1,
                "Hello",
                "",
                "testGame",
                new ChessGame());

    }

}
