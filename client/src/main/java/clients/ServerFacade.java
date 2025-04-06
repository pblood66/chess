package clients;

import models.results.CreateGameResult;
import models.results.ListGamesResult;
import models.results.LoginResult;
import models.results.RegisterResult;
import websocket.commands.UserGameCommand;


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
        this.websocket = new WebSocketCommunicator(serverUrl);
        UserGameCommand connect = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId);
        websocket.sendMessage(connect.toJson());
    }

    public void resign(String authToken, int gameID) throws Exception {
        if (websocket != null) {
            websocket.resign(authToken, gameID);
        }
    }

    public void leaveGame(String authToken, int gameID) throws Exception {
        System.out.println("Server Facade Leaving Game");
        UserGameCommand leave = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        System.out.println("attempting leave...");
        websocket.sendMessage(leave.toJson());
        websocket = null;
    }

    public void observe(String authToken, int gameID) throws Exception {
        openWebSocket();
        UserGameCommand connect = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        websocket.sendMessage(connect.toJson());
    }

    private void openWebSocket() {
        try {
            websocket = new WebSocketCommunicator(serverUrl);
        } catch(Exception e) {
            System.out.println("Error: Could not connect websocket");
        }
    }


}
