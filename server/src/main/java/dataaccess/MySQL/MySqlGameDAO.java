package dataaccess.MySQL;

import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import dataaccess.exceptions.DataAccessException;
import models.GameData;

import java.util.Collection;
import java.util.List;

public class MySqlGameDAO implements GameDAO {
    public MySqlGameDAO() {
        String[] tableStatements = {
                """
                CREATE TABLE IF NOT EXISTS games (
                gameId INT NOT NULL,
                whiteUsername VARCHAR(20) NOT NULL,
                blackUsername VARCHAR(20) NOT NULL,
                gameName VARCHAR(20) NOT NULL,
                game JSON NOT NULL,
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
        return 0;
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
        return 0;
    }

    @Override
    public void clear() {

    }
}
