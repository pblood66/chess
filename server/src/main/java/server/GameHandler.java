package server;

import com.google.gson.Gson;
import dataaccess.exceptions.BadRequestException;
import service.GameService;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.requests.ListGamesRequest;
import service.results.CreateGameResult;
import service.results.ListGamesResult;
import spark.Request;
import spark.Response;

public class GameHandler {
    private GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object createGame(Request req, Response res) throws Exception {
        String authToken = req.headers("Authorization");

        CreateGameRequest request = new Gson().fromJson(req.body(), CreateGameRequest.class);
        request = request.setAuthToken(authToken);

        CreateGameResult result = gameService.createGame(request);

        res.status(200);

        return new Gson().toJson(result);

    }

    public Object listGames(Request req, Response res) throws Exception {
        String authToken = req.headers("Authorization");

        ListGamesRequest request = new ListGamesRequest(authToken);

        ListGamesResult result = gameService.listGames(request);

        res.status(200);

        return new Gson().toJson(result);
    }

    public Object joinGame(Request req, Response res) throws Exception {
        String authToken = req.headers("Authorization");

        JoinGameRequest request = new Gson().fromJson(req.body(), JoinGameRequest.class);

        if ( request.playerColor() == null || request.playerColor().isEmpty()) {
            throw new BadRequestException("error: bad request");
        }

        request = request.setAuthToken(authToken);

        gameService.joinGame(request);
        res.status(200);
        return "{}";
    }
}
