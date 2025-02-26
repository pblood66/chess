package dataaccess.memory;

import dataaccess.GameDAO;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import models.GameData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {
    private final HashSet<GameData> database;

    public MemoryGameDAO() {
        database = new HashSet<>();
    }

    @Override
    public int createGame(GameData game) {
        database.add(game);

        return game.gameID();
    }

    @Override
    public Collection<GameData> listGames() {
        return database;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData game: database) {
            if (game.gameID() == gameID) {
                return game;
            }
        }

        throw new BadRequestException("Error: bad request");
    }

    @Override
    public void removeGame(int gameID) throws DataAccessException {
        for (GameData game: database) {
            if (game.gameID() == gameID) {
                database.remove(game);
                return;
            }
        }
        throw new DataAccessException("Game: " + gameID + " not found");
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        removeGame(game.gameID());
        database.add(game);
    }

    @Override
    public int size() {
        return database.size();
    }

    @Override
    public void clear() {
        database.clear();
    }
}
