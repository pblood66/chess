package server.handlers.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;

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

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private final GameSessionManager gameSessions;

    public WebSocketHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.gameSessions = new GameSessionManager();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        System.out.println(message);

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

            String role;
            if (auth.username().equals(currentGame.whiteUsername())) {
                role = "white";
            } else if (auth.username().equals(currentGame.blackUsername())) {
                role = "black";
            } else {
                role = "observer";
            }

            LoadGameMessage loadGame = new LoadGameMessage(currentGame);

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
        System.out.println("deleted " + username);

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
            if (isObserver(command)) {
                throw new DataAccessException("Error: Observer can't resign game");
            }

            AuthData auth = authDAO.getAuth(command.getAuthToken());
            NotificationMessage resignMessage = new NotificationMessage(auth.username() + " has resigned");

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
            if (isObserver(command)) {
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

            String userMove = "Player " + auth.username() + " moved " +
                    move.getStartPosition().toString() + " to " + move.getEndPosition().toString();
            NotificationMessage notification = new NotificationMessage(userMove);
            gameSessions.broadcast(gameID, notification, authToken);

            // update move to board
            currentGame.game().makeMove(move);

            // check if game is over
            if (!handleGameOver(currentGame, gameID)) {
                handleCheckNotification(currentGame, gameID);
            }

            gameDAO.updateGame(currentGame);

            LoadGameMessage loadGame = new LoadGameMessage(currentGame);
            gameSessions.broadcast(gameID, loadGame);
        } catch (DataAccessException | InvalidMoveException ex) {
            onError(session, ex);
        }
    }

    private boolean handleGameOver(GameData game, int gameID) throws Exception {
        ChessGame chessGame = game.game();

        if (chessGame.isInCheckmate(ChessGame.TeamColor.WHITE) || chessGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            chessGame.setGameOver(true);
            String message = chessGame.isInCheckmate(ChessGame.TeamColor.WHITE)
                    ? game.whiteUsername() + " is in checkmate\nGAME OVER"
                    : game.blackUsername() + " is in checkmate\nGAME OVER";
            broadcastGameOver(gameID, message);
            return true;
        }

        if (chessGame.isInStalemate(ChessGame.TeamColor.WHITE) || chessGame.isInStalemate(ChessGame.TeamColor.BLACK)) {
            chessGame.setGameOver(true);
            broadcastGameOver(gameID, "Stalemate\nGAME OVER");
            return true;
        }

        return false;
    }

    private void broadcastGameOver(int gameID, String message) throws Exception {
        gameSessions.broadcast(gameID, new NotificationMessage(message));
    }

    private void handleCheckNotification(GameData game, int gameID) throws Exception{
        boolean whiteInCheck = game.game().isInCheck(ChessGame.TeamColor.WHITE);
        boolean blackInCheck = game.game().isInCheck(ChessGame.TeamColor.BLACK);

        if (whiteInCheck || blackInCheck) {
            String message = whiteInCheck
                    ? game.whiteUsername() + " is in check"
                    : game.blackUsername() + " is in check";
            gameSessions.broadcast(gameID, new NotificationMessage(message));
        }
    }

    private boolean isGameOver(GameData game) {
        return game.game().isGameOver();
    }

    private boolean isObserver(UserGameCommand command) throws Exception {
        int gameID = command.getGameID();
        String authToken = command.getAuthToken();

        AuthData auth = authDAO.getAuth(authToken);
        GameData currentGame = gameDAO.getGame(gameID);

        return !currentGame.whiteUsername().equals(auth.username()) && !currentGame.blackUsername().equals(auth.username());
    }
}
