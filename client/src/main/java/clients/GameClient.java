package clients;

import chess.*;
import ui.BoardUi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class GameClient {
    private final ServerFacade server;
    private final ClientData clientData;

    public GameClient(ServerFacade server, ClientData clientData) {
        this.server = server;
        this.clientData = clientData;
    }

    public String eval(String line) {
        String[] tokens = line.split(" ");
        String[] params = tokens.length > 1 ? Arrays.copyOfRange(tokens, 1, tokens.length) : new String[0];

        try {
            return switch (tokens[0]) {
                case "redraw" -> drawBoard();
                case "move" -> move(params);
                case "resign" -> resign();
                case "legal" -> legal(params);
                case "leave" -> leave();
                default -> help();
            };
        }
        catch (Exception e) {
            return "[ERROR]: " + e.getMessage();
        }
    }

    private String legal(String[] params) {
        ChessPosition position = new ChessPosition(params[0]);

        Collection<ChessMove> validMoves = clientData.getCurrentGame().game().validMoves(position);
        ChessBoard currentBoard = clientData.getCurrentGame().game().getBoard();
        ChessGame.TeamColor orientation = clientData.getPlayerColor();

        ArrayList<ChessPosition> highlightedMoves = new ArrayList<>();
        highlightedMoves.add(position);

        for (ChessMove move : validMoves) {
            highlightedMoves.add(move.getEndPosition());
        }

        return BoardUi.drawBoard(currentBoard, orientation, highlightedMoves);
    }

    private String resign() {
        server.resign(clientData.getAuthToken(), clientData.getCurrentGame().gameID());
        return "";
    }

    private String move(String[] params) {
        if (params.length < 2) {
            throw new IllegalArgumentException("<position 1> <position 2> <promotion piece>");
        }

        ChessPosition firstPosition = new ChessPosition(params[0]);
        ChessPosition secondPosition = new ChessPosition(params[1]);
        ChessMove move;
        if (params.length == 3) {
            ChessPiece.PieceType type = ChessPiece.PieceType.fromString(params[2]);
            move = new ChessMove(firstPosition, secondPosition, type);
        }
        else {
            move = new ChessMove(firstPosition, secondPosition, null);
        }

        server.makeMove(clientData.getAuthToken(), clientData.getCurrentGame().gameID(), move);

        return "";
    }

    private String help() {
        return """
                redraw - redraws the board for spectator or player
                help - shows this help message
                move <position 1> <position 2> <promotion piece> - makes move and updates board
                resign - resign game
                legal <position> -  highlights legal moves of piece
                leave - quits current game
                """;
    }



    public String drawBoard() {
        if (clientData.getPlayerColor() != null) {
            return BoardUi.drawBoard(clientData.getCurrentGame().game().getBoard(), clientData.getPlayerColor(), null);
        }
        else {
            return "Observing Game: " + clientData.getCurrentGame().gameID() + "\n" +
                    BoardUi.drawBoard(clientData.getCurrentGame().game().getBoard(), ChessGame.TeamColor.WHITE, null) +
                    "\n" +
                    BoardUi.drawBoard(clientData.getCurrentGame().game().getBoard(), ChessGame.TeamColor.BLACK, null);
        }
    }

    private String leave() {
        System.out.println("Leaving game");
        server.leaveGame(clientData.getAuthToken(), clientData.getCurrentGame().gameID());
        clientData.setPlayerColor(null);
        clientData.setCurrentGame(null);
        clientData.setState(ClientData.ClientState.LOGGED_IN);


        return "quit game";
    }
}
