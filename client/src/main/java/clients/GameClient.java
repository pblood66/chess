package clients;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import ui.BoardUi;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GameClient {
    private ServerFacade server;
    private ClientData clientData;

    public GameClient(String serverUrl, ClientData clientData) {
        this.server = new ServerFacade(serverUrl);
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

        Collection<ChessMove> valid = clientData.getCurrentGame().game().validMoves(position);
        ChessBoard currentBoard = clientData.getCurrentGame().game().getBoard();
        ChessGame.TeamColor orientation = clientData.getPlayerColor();

        return BoardUi.drawBoard(currentBoard, orientation, valid.toArray(new ChessMove[0]));
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
            return BoardUi.drawBoard(clientData.getCurrentGame().game().getBoard(), clientData.getPlayerColor());
        }
        else {
            return "Observing Game: " + clientData.getCurrentGame().gameID() + "\n" +
                    BoardUi.drawBoard(clientData.getCurrentGame().game().getBoard(), ChessGame.TeamColor.WHITE) +
                    "\n" +
                    BoardUi.drawBoard(clientData.getCurrentGame().game().getBoard(), ChessGame.TeamColor.BLACK);
        }
    }

    private String leave() throws Exception{
        server.leaveGame(clientData.getAuthToken(), clientData.getCurrentGame().gameID());
        clientData.setPlayerColor(null);
        clientData.setCurrentGame(null);
        clientData.setState(ClientData.ClientState.LOGGED_IN);


        return "quit game";
    }




}
