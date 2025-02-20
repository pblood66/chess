package dataaccess;

import chess.ChessGame;
import models.AuthData;
import models.GameData;

import java.util.Collection;

public interface GameDAO {
    int createGame(GameData game) throws DataAccessException;
    Collection<GameData> listGames();
    GameData getGame(int gameID) throws DataAccessException;
    void removeGame(int gameID) throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    void clear();
}
