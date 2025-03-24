package ui.clients;

import chess.ChessGame;
import models.GameData;
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
                case "redraw" -> drawBoard();
                case "quit" -> quit();
                default -> drawBoard();
            };
        }
        catch (Exception e) {
            return "[ERROR]: " + e.getMessage();
        }
    }

    public String drawBoard() {
        if (clientData.getPlayerColor() != null) {
            return BoardUi.drawBoard(clientData.getGame().getBoard(), clientData.getPlayerColor());
        }
        else {
            return "Observing Game: " + clientData.getGameId() + "\n" +
                    BoardUi.drawBoard(clientData.getGame().getBoard(), ChessGame.TeamColor.WHITE) +
                    "\n" +
                    BoardUi.drawBoard(clientData.getGame().getBoard(), ChessGame.TeamColor.BLACK);
        }
    }

    private String quit() {
        clientData.setPlayerColor(null);
        clientData.setGame(null);
        clientData.setGameId(0);
        clientData.setState(ClientData.ClientState.LOGGED_IN);

        return "quit game";
    }


}
