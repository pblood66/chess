package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UnauthoriedException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import models.GameData;
import service.requests.ListGamesRequest;
import service.requests.CreateGameRequest;
import service.results.CreateGameResult;
import service.results.ListGamesResult;

import java.util.Collection;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    int currentGameId;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        currentGameId = 0;
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
            GameData newGame = new GameData(currentGameId, null, null,
                    gameName, new ChessGame());
            gameDAO.createGame(newGame);
            return new CreateGameResult(currentGameId);

        } catch (DataAccessException e) {
            throw new UnauthoriedException(e.getMessage());
        }
    }


    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {
        String token = request.authToken();
        try {
            authDAO.getAuth(token);
            Collection<GameData> games = gameDAO.listGames();

            return new ListGamesResult(games);
        } catch (DataAccessException e) {
            throw new UnauthoriedException("Error: unauthorized");
        }
    }

}
