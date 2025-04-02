package server.handlers.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

import dataaccess.exceptions.DataAccessException;
import models.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private GameDAO gameDAO;
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameSessionManager gameSessions;

    public WebSocketHandler(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameSessions = new GameSessionManager();
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

    @OnWebSocketError
    public void onError(Session session, Throwable error) throws Exception {
        ErrorMessage errorMessage = new ErrorMessage(error.getMessage());
        session.getRemote().sendString(errorMessage.toJson());
    }

    private void sendMessage(Session session, ServerMessage message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }

    private void handleConnect(Session session, UserGameCommand command) throws Exception {
        // Server sends a LOAD_GAME message back to the root client.
        try {
            GameData currentGame = gameDAO.getGame(command.getGameID());
            AuthData auth = authDAO.getAuth(command.getAuthToken());

            LoadGameMessage loadGame = new LoadGameMessage(currentGame);

            sendMessage(session, loadGame);
            gameSessions.addSession(currentGame.gameID(), command.getAuthToken(), session);

            String role;
            if (auth.username().equals(currentGame.whiteUsername())) {
                role = "white";
            } else if (auth.username().equals(currentGame.blackUsername())) {
                role = "black";
            } else {
                role = "observer";
            }

            NotificationMessage notification = new NotificationMessage(auth.username() + " has connected as " + role);

            gameSessions.broadcast(currentGame.gameID(), notification, command.getAuthToken());
        } catch (DataAccessException ex) {
            onError(session, ex);
        }

    }

    private void handleLeave(Session session, UserGameCommand command) throws Exception {
        int gameId = command.getGameID();
        String authToken = command.getAuthToken();

        GameData currentGame = gameDAO.getGame(gameId);
        AuthData auth = authDAO.getAuth(authToken);

        String username = auth.username();

        if (currentGame.whiteUsername().equals(username)) {
            currentGame.setWhiteUsername(null);
        }
        else if (currentGame.blackUsername().equals(username)) {
            currentGame.setBlackUsername(null);
        }

        gameDAO.updateGame(currentGame);
        gameSessions.removeSession(gameId, authToken);

        NotificationMessage notification = new NotificationMessage(auth.username() + " has left the game");
        gameSessions.broadcast(currentGame.gameID(), notification, authToken);
    }

    private void handleResign(Session session, UserGameCommand command) throws Exception {
        try {
            if (!isPlayer(command)) {
                ErrorMessage error = new ErrorMessage("Error: Observer can't resign game");
                sendMessage(session, error);
                return;
            }

            // TODO: mark game as over
            AuthData auth = authDAO.getAuth(command.getAuthToken());
            NotificationMessage resignMessage = new NotificationMessage("");

            gameSessions.broadcast(command.getGameID(), resignMessage);
        } catch (DataAccessException ex) {
            onError(session, ex);
        }
    }

    private void handleMove(Session session, UserGameCommand command) {
    }

    private boolean isPlayer(UserGameCommand command) throws Exception {
        int gameID = command.getGameID();
        String authToken = command.getAuthToken();

        AuthData auth = authDAO.getAuth(authToken);
        GameData currentGame = gameDAO.getGame(gameID);

        return currentGame.whiteUsername().equals(auth.username()) || currentGame.blackUsername().equals(auth.username());
    }
}
