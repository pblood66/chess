package dataaccess.MySQL;

import com.google.gson.Gson;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import models.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class MySqlGameDAO implements GameDAO {
    public MySqlGameDAO() {
        String[] tableStatements = {
                """
                CREATE TABLE IF NOT EXISTS games (
                gameId INT NOT NULL,
                whiteUsername VARCHAR(20),
                blackUsername VARCHAR(20),
                gameName VARCHAR(20) NOT NULL,
                game TEXT NOT NULL,
                PRIMARY KEY (gameId)
                )
                """
        };

        try {
            DatabaseManager.configureDatabase(tableStatements);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {

        var statement = "INSERT INTO games (gameId, whiteUsername, blackUsername, gameName, game) " +
                "VALUES(?, ?, ?, ?, ?)";
        String whiteUsername = game.whiteUsername() == null ? "" : game.whiteUsername();
        String blackUsername = game.blackUsername() == null ? "" : game.blackUsername();

        try {
            DatabaseManager.executeUpdate(statement, game.gameID(), whiteUsername,
                    blackUsername, game.gameName(), new Gson().toJson(game));

            return game.gameID();
        } catch (DataAccessException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void removeGame(int gameID) throws DataAccessException {

    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public int size() {
        var statement = "SELECT COUNT(*) FROM games";
        try (var connection = DatabaseManager.getConnection()) {
            try (var result = DatabaseManager.executeQuery(connection, statement)) {
                result.next();
                return result.getInt(1);
            }
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void clear() {
        try {
            var statement = "TRUNCATE TABLE games";
            DatabaseManager.executeUpdate(statement);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
