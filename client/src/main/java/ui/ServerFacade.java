package ui;

import com.google.gson.Gson;
import models.requests.*;
import models.results.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;


public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

    public void clear() throws Exception {
        var path = serverUrl + "/db";
        makeRequest("DELETE", path, null, null);
    }

    public RegisterResult register(RegisterRequest request) throws Exception {
        var path = serverUrl + "/user";
        var body = Map.of(
                "username", request.username(),
                "password", request.password(),
                "email", request.email()
        );

        return makeRequest("POST", path, body, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request) throws Exception {
        var path = serverUrl + "/session";
        var body = Map.of(
            "username", request.username(),
            "password", request.password()
        );

        return makeRequest("POST", path, body, LoginResult.class);
    }

    public void logout(LogoutRequest request) throws Exception {
        var path = serverUrl + "/session";
        var body = Map.of(
                "authToken", request.authToken()
        );

        makeRequest("DELETE", path, body, null);
    }

    public CreateGameResult createGame(CreateGameRequest request) throws Exception {
        var path = serverUrl + "/game";
        var body = Map.of(
                "authToken", request.authToken(),
                "gameName", request.gameName()
        );

        return makeRequest("POST", path, body, CreateGameResult.class);
    }

    public ListGamesResult listGames(ListGamesRequest request) throws Exception {
        var path = serverUrl + "/game";
        var body = Map.of(
                  "authToken", request.authToken()
        );

        return makeRequest("GET", path, body, ListGamesResult.class);
    }

    public void joinGame(JoinGameRequest request) throws Exception {
        var path = serverUrl + "/game";
        var body = Map.of(
                "authToken", request.authToken(),
                "playerColor", request.playerColor(),
                "gameID", request.gameID()
        );

        makeRequest("POST", path, body, Void.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            HttpURLConnection http = (HttpURLConnection) (new URI(path)).toURL().openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);

            http.connect();

            if (http.getResponseCode() == 200) {
                return readBody(http, responseClass);
            }

            throw new Exception(http.getResponseMessage());
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    private static void writeBody(Object body, HttpURLConnection http) throws IOException {
        http.addRequestProperty("Content-Type", "application/json");
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
