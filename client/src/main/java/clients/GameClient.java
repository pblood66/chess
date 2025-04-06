package clients;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import ui.BoardUi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
                case "help" -> help();
                default -> drawBoard();
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

    private String resign() throws Exception {
        server.resign(clientData.getAuthToken(), clientData.getCurrentGame().gameID());
        return "resigned from game";
    }

    private String move(String[] params) {
        if (params.length > 2) {
            throw new IllegalArgumentException("<position 1> <position 2>");
        }

        ChessPosition firstPosition = new ChessPosition(params[0]);
        ChessPosition secondPosition = new ChessPosition(params[1]);

        return "move " + params[0];
    }

    private String help() {
        return """
                redraw - redraws the board for spectator or player
                help - shows this help message
                move <position 1> <position 2> - makes move and updates board
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

    private String leave() throws Exception{
        System.out.println("Leaving game");
        server.leaveGame(clientData.getAuthToken(), clientData.getCurrentGame().gameID());
        clientData.setPlayerColor(null);
        clientData.setCurrentGame(null);
        clientData.setState(ClientData.ClientState.LOGGED_IN);


        return "quit game";
    }
}
