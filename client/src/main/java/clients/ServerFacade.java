package clients;

import models.results.CreateGameResult;
import models.results.ListGamesResult;
import models.results.LoginResult;
import models.results.RegisterResult;


public class ServerFacade {
    private final String serverUrl;
    private final HttpCommunicator http;
    private WebSocketCommunicator websocket;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
        http = new HttpCommunicator(serverUrl);
    }

    public void clear() throws Exception {
        http.clear();
    }

    public RegisterResult register(String username, String password, String email) throws Exception {
        return http.register(username, password, email);
    }

    public LoginResult login(String username, String password) throws Exception {
        return http.login(username, password);
    }

    public void logout(String authToken) throws Exception {
       http.logout(authToken);
    }

    public CreateGameResult createGame(String gameName, String authToken) throws Exception {
        return http.createGame(gameName, authToken);
    }

    public ListGamesResult listGames(String authToken) throws Exception {
        return http.listGames(authToken);
    }

    public void joinGame(String playerColor, int gameId, String authToken) throws Exception {
        http.joinGame(playerColor, gameId, authToken);
        openWebSocket();
    }

    private void openWebSocket() {
        try {
            websocket = new WebSocketCommunicator(serverUrl);
        } catch(Exception e) {
            System.out.println("Error: Could not connect websocket");
        }
    }


}
