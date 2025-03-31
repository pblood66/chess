package clients;

import chess.ChessGame;
import ui.BoardUi;

import java.util.Arrays;

public class GameClient {
    private String serverUrl;
    private ClientData clientData;

    public GameClient(String serverUrl, ClientData clientData) {
        this.serverUrl = serverUrl;
        this.clientData = clientData;
    }


    public String eval(String line) {
        String[] tokens = line.split(" ");
        String[] params = tokens.length > 1 ? Arrays.copyOfRange(tokens, 1, tokens.length) : new String[0];

        try {
            return switch (tokens[0]) {
                case "leave" -> leave();
                case "help" -> help();
                default -> drawBoard();
            };
        }
        catch (Exception e) {
            return "[ERROR]: " + e.getMessage();
        }
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

    private String leave() {
        clientData.setPlayerColor(null);
        clientData.setCurrentGame(null);
        clientData.setState(ClientData.ClientState.LOGGED_IN);

        return "quit game";
    }


}
