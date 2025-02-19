package dataaccess;

import chess.ChessGame;
import models.AuthData;
import models.GameData;

import java.util.List;

public interface GameDAO {
    int createGame(String gameName, int gameID) throws DataAccessException;
    List<GameData> listGames() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void updateGame(AuthData authData, ChessGame.TeamColor playerColor, int gameID) throws DataAccessException;
    void clear();
}
