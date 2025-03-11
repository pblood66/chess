package dataaccess.mysql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import dataaccess.exceptions.DataAccessException;
import models.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

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
                    blackUsername, game.gameName(), serializeGame(game.game()));

            return game.gameID();
        } catch (DataAccessException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
    }

    @Override
    public Collection<GameData> listGames() {
        HashSet<GameData> games = new HashSet<>();
        var statement = "SELECT * FROM games";
        try (var conn = DatabaseManager.getConnection()) {
            var result = DatabaseManager.executeQuery(conn, statement);
            while (result.next()) {
                int gameId = result.getInt("gameId");
                String whiteUsername = result.getString("whiteUsername");
                String blackUsername = result.getString("blackUsername");

                whiteUsername = whiteUsername.isEmpty() ? null : whiteUsername;
                blackUsername = blackUsername.isEmpty() ? null : blackUsername;

                String gameName = result.getString("gameName");
                ChessGame game =  deserializeGame(result.getString("game"));

                games.add(new GameData(gameId, whiteUsername, blackUsername, gameName, game));
            }

            return games;
        } catch(DataAccessException | SQLException ex) {
            return null;
        }
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        var statement = "SELECT whiteUsername, blackUsername, gameName, game FROM games WHERE gameId = ?";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(statement)) {

            stmt.setInt(1, gameId);

            try (var result = stmt.executeQuery()) {
                result.next();

                var whiteUsername = result.getString("whiteUsername");
                var blackUsername = result.getString("blackUsername");
                var gameName = result.getString("gameName");
                var chessGameData = result.getString("game");

                var chessGame = (chessGameData != null && !chessGameData.isEmpty())
                        ? deserializeGame(chessGameData)
                        : new ChessGame();

                return new GameData(
                        gameId,
                        whiteUsername,
                        blackUsername,
                        gameName,
                        chessGame
                );
            }
        } catch (DataAccessException | SQLException ex) {
            throw new DataAccessException("Error retrieving game data");
        }
    }

    @Override
    public void removeGame(int gameId) throws DataAccessException {
        var statement = "DELETE FROM games WHERE gameId = ?";
        try {
            getGame(gameId);
            DatabaseManager.executeUpdate(statement, gameId);
        } catch (DataAccessException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        String whiteUsername = game.whiteUsername() == null ? "" : game.whiteUsername();
        String blackUsername = game.blackUsername() == null ? "" : game.blackUsername();

        try {
            GameData select = getGame(game.gameID());

            var statement = "UPDATE games SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameId=?";
            DatabaseManager.executeUpdate(statement, whiteUsername, blackUsername, game.gameName(),
                    serializeGame(game.game()), game.gameID());
        } catch (DataAccessException ex) {
            throw new DataAccessException(ex.getMessage());
        }
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

    private String serializeGame(ChessGame game) {
        return new Gson().toJson(game);
    }

    private ChessGame deserializeGame(String serializedGame) {
        return new Gson().fromJson(serializedGame, ChessGame.class);
    }
}
