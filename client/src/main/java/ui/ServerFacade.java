package ui;

import com.google.gson.Gson;
import models.requests.*;
import models.results.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;


public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void clear() throws Exception {
        var path = serverUrl + "/db";
        makeRequest("DELETE", path, null, null);
    }

    public RegisterResult register(String username, String password, String email) throws Exception {
        var path = serverUrl + "/user";
        var body = Map.of(
                "username", username,
                "password", password,
                "email", email
        );

        return makeRequest("POST", path, body, RegisterResult.class);
    }

    public LoginResult login(String username, String password) throws Exception {
        var path = serverUrl + "/session";
        var body = Map.of(
            "username", username,
            "password", password
        );

        return makeRequest("POST", path, body, LoginResult.class);
    }

    public void logout(String authToken) throws Exception {
        var path = serverUrl + "/session";

        makeRequest("DELETE", path, null, null, authToken);
    }

    public CreateGameResult createGame(String gameName, String authToken) throws Exception {
        var path = serverUrl + "/game";
        var body = Map.of(
                "gameName", gameName
        );

        return makeRequest("POST", path, body, CreateGameResult.class, authToken);
    }

    public ListGamesResult listGames(String authToken) throws Exception {
        var path = serverUrl + "/game";

        return makeRequest("GET", path, null, ListGamesResult.class, authToken);
    }

    public void joinGame(String playerColor, int gameId, String authToken) throws Exception {
        var path = serverUrl + "/game";
        var body = Map.of(
                "playerColor", playerColor,
                "gameID", gameId
        );

        makeRequest("PUT", path, body, null, authToken);
    }

    private <T> T makeRequest(String method, String path, Object request,
                              Class<T> responseClass, String... authToken) throws Exception {
        try {
            HttpURLConnection http = (HttpURLConnection) (new URI(path)).toURL().openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null && authToken.length > 0) {
                http.setRequestProperty("Authorization", authToken[0]);
            }
            if (request != null) {
                writeBody(request, http);
            }

            http.connect();

            if (http.getResponseCode() / 100 == 2) {
                return readBody(http, responseClass);
            }

            throw new Exception(http.getResponseMessage() + " " + http.getResponseCode());
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
