package server.handlers.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
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
import websocket.commands.MakeMoveCommand;
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
            case MAKE_MOVE -> handleMove(session, new Gson().fromJson(message, MakeMoveCommand.class));
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
            ChessGame.TeamColor boardOrientation;

            String role;
            if (auth.username().equals(currentGame.whiteUsername())) {
                role = "white";
                boardOrientation = ChessGame.TeamColor.WHITE;
            } else if (auth.username().equals(currentGame.blackUsername())) {
                role = "black";
                boardOrientation = ChessGame.TeamColor.BLACK;
            } else {
                role = "observer";
                boardOrientation = ChessGame.TeamColor.WHITE;
            }

            LoadGameMessage loadGame = new LoadGameMessage(currentGame, boardOrientation);

            sendMessage(session, loadGame);
            gameSessions.addSession(currentGame.gameID(), command.getAuthToken(), session);

            NotificationMessage notification = new NotificationMessage(auth.username() + " has connected as " + role);
            System.out.println("Player Joined");
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
            currentGame = currentGame.setWhiteUsername("");
        }
        else if (currentGame.blackUsername().equals(username)) {
            currentGame = currentGame.setBlackUsername("");
        }

        gameDAO.updateGame(currentGame);
        gameSessions.removeSession(gameId, authToken);

        NotificationMessage notification = new NotificationMessage(auth.username() + " has left the game");
        gameSessions.broadcast(currentGame.gameID(), notification, authToken);
    }

    private void handleResign(Session session, UserGameCommand command) throws Exception {
        try {
            if (!isPlayer(command)) {
                throw new DataAccessException("Error: Observer can't resign game");
            }

            // TODO: mark game as over
            AuthData auth = authDAO.getAuth(command.getAuthToken());
            NotificationMessage resignMessage = new NotificationMessage("");

            GameData game = gameDAO.getGame(command.getGameID());

            if (isGameOver(game)) {
                throw new DataAccessException("Error: Game Over");
            }

            game.game().setGameOver(true);
            gameDAO.updateGame(game);

            gameSessions.broadcast(command.getGameID(), resignMessage);
        } catch (DataAccessException ex) {
            onError(session, ex);
        }
    }

    private void handleMove(Session session, MakeMoveCommand command) throws Exception {
        try {
            if (!isPlayer(command)) {
                throw new InvalidMoveException("Error: Observer can't move piece");
            }

            ChessMove move = command.getMove();
            int gameID = command.getGameID();
            String authToken = command.getAuthToken();

            GameData currentGame = gameDAO.getGame(gameID);
            AuthData auth = authDAO.getAuth(authToken);

            if (isGameOver(currentGame)) {
                throw new InvalidMoveException("Error: Game Over");
            }

            // make sure move isn't on opponent's piece
            ChessGame.TeamColor playerColor = currentGame.whiteUsername().equals(auth.username())
                    ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

            ChessPiece piece = currentGame.game().getBoard().getPiece(move.getStartPosition());
            if (piece.getTeamColor() != playerColor) {
                throw new InvalidMoveException("Error: cannot move opponent's piece");
            }

            // update move to board
            currentGame.game().makeMove(move);
            gameDAO.updateGame(currentGame);

            LoadGameMessage loadGame = new LoadGameMessage(currentGame, playerColor);
            gameSessions.broadcast(gameID, loadGame);

            NotificationMessage notification = new NotificationMessage(move.toString());
            gameSessions.broadcast(gameID, notification, authToken);

        } catch (DataAccessException | InvalidMoveException ex) {
            onError(session, ex);
        }
    }

    private boolean isGameOver(GameData game) {
        return game.game().isGameOver();
    }

    private boolean isPlayer(UserGameCommand command) throws Exception {
        int gameID = command.getGameID();
        String authToken = command.getAuthToken();

        AuthData auth = authDAO.getAuth(authToken);
        GameData currentGame = gameDAO.getGame(gameID);

        return currentGame.whiteUsername().equals(auth.username()) || currentGame.blackUsername().equals(auth.username());
    }
}
