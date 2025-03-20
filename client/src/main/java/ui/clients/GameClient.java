package ui.clients;

import chess.ChessGame;
import models.GameData;

public class GameClient {
    private String serverUrl;
    private ClientData clientData;

    public GameClient(String serverUrl, ClientData clientData) {
        this.serverUrl = serverUrl;
        this.clientData = clientData;
    }


    public String eval(String line) {
        return "";
    }

    public String drawBoard() {
        return "Chess Board: ";
    }
}
