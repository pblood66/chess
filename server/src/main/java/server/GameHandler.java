package server;

import com.google.gson.Gson;
import service.GameService;
import service.requests.CreateGameRequest;
import service.results.CreateGameResult;
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
}
