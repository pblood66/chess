package server.handlers.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

import models.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private GameDAO gameDAO;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public WebSocketHandler(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        switch (command.getCommandType()) {
            case CONNECT -> handleConnect(session, command);
            case MAKE_MOVE -> handleMove(session, command);
            case RESIGN -> handleResign(session, command);
            case LEAVE -> handleLeave(session, command);
        }
    }

    private void sendMessage(Session session, ServerMessage message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }

    private void handleConnect(Session session, UserGameCommand command) throws Exception {
        // Server sends a LOAD_GAME message back to the root client.
        GameData currentGame = gameDAO.getGame(command.getGameID());
        LoadGameMessage loadGame = new LoadGameMessage(currentGame);

        sendMessage(session, loadGame);

        // Server sends a Notification message to all other clients in that game informing them the root client
        // connected to the game, either as a player (in which case their color must be specified) or as an observer.
        AuthData auth = authDAO.getAuth(command.getAuthToken());
        NotificationMessage notification = new NotificationMessage(auth.username() + " has connected to to game");
        // sendMessage(session, notification);
    }

    private void handleLeave(Session session, UserGameCommand command) {
    }

    private void handleResign(Session session, UserGameCommand command) {
    }

    private void handleMove(Session session, UserGameCommand command) {
    }
}
