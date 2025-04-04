package service;

import chess.ChessBoard;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.DuplicatedException;
import models.AuthData;
import models.GameData;
import models.requests.JoinGameRequest;
import models.requests.ListGamesRequest;
import models.requests.CreateGameRequest;
import models.results.CreateGameResult;
import models.results.ListGamesResult;

import java.util.Collection;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    int currentGameId;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        currentGameId = gameDAO.size();
    }


    public void joinGame(JoinGameRequest request) throws DataAccessException{
        String authToken = request.authToken();
        int gameID = request.gameID();
        String playerColor = request.playerColor();
        AuthData auth = authDAO.getAuth(authToken);
        GameData game = gameDAO.getGame(gameID);

        String username = auth.username();

        if (!isColorAvailable(game, playerColor)) {
            throw new DuplicatedException("Error: already taken");
        }

        GameData updatedGame;
        if (playerColor.equals("WHITE")) {
            updatedGame = game.setWhiteUsername(username);
        }
        else if (playerColor.equals("BLACK")) {
            updatedGame = game.setBlackUsername(username);
        } else {
            throw new BadRequestException("Error: bad request");
        }

        gameDAO.updateGame(updatedGame);

    }

    public boolean isColorAvailable(GameData game, String playerColor) {
        if (playerColor.equals("WHITE")) {
            return game.whiteUsername().isEmpty();
        }
        return game.blackUsername().isEmpty();
    }

    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException {
        String gameName = request.gameName();
        String token = request.authToken();

        if (gameName == null || gameName.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        try {
            authDAO.getAuth(token);
            currentGameId++;
            ChessGame game = new ChessGame();
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            game.setBoard(board);
            GameData newGame = new GameData(currentGameId, null, null,
                    gameName, game);
            gameDAO.createGame(newGame);
            return new CreateGameResult(currentGameId);

        } catch (DataAccessException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }


    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {
        String token = request.authToken();
        authDAO.getAuth(token);
        Collection<GameData> games = gameDAO.listGames();

        return new ListGamesResult(games);
    }

}
