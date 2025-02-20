package dataaccess;

import chess.ChessGame;
import models.AuthData;
import models.GameData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {
    private final HashSet<GameData> database;

    public MemoryGameDAO() {
        database = new HashSet<>();
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        try {
            getGame(game.gameID());
            database.add(game);
        }
        catch (DataAccessException e) {
            throw new DataAccessException("Game: " + game.gameID() + " already exists");
        }

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

        throw new DataAccessException("Game: " + gameID + " not found");
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
    public void clear() {
        database.clear();
    }
}
