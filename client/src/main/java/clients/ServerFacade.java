package clients;

import chess.ChessMove;
import models.results.CreateGameResult;
import models.results.ListGamesResult;
import models.results.LoginResult;
import models.results.RegisterResult;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;


public class ServerFacade {
    private final String serverUrl;
    private final HttpCommunicator http;
    private WebSocketCommunicator websocket;
    private final ClientData clientData;

    public ServerFacade(String serverUrl, ClientData clientData) {
        this.serverUrl = serverUrl;
        http = new HttpCommunicator(serverUrl);
        this.clientData = clientData;
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
        this.websocket = new WebSocketCommunicator(serverUrl, clientData);
        UserGameCommand connect = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId);
        websocket.sendMessage(connect.toJson());
    }

    public void resign(String authToken, int gameID) {
        if (websocket != null) {
            websocket.resign(authToken, gameID);
        }
    }

    public void leaveGame(String authToken, int gameID) {
        UserGameCommand leave = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        websocket.sendMessage(leave.toJson());
        websocket = null;
    }

    public void observe(String authToken, int gameID) {
        openWebSocket();
        UserGameCommand connect = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        websocket.sendMessage(connect.toJson());
    }

    public void makeMove(String authToken, int gameID, ChessMove move) {
        MakeMoveCommand moveCommand = new MakeMoveCommand(authToken, gameID, move);
        websocket.sendMessage(moveCommand.toJson());
    }

    private void openWebSocket() {
        try {
            websocket = new WebSocketCommunicator(serverUrl, clientData);
        } catch(Exception e) {
            System.out.println("Error: Could not connect websocket");
        }
    }


}
