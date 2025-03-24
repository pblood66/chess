package ui.clients;

import chess.ChessGame;
import models.GameData;
import ui.BoardUi;

public class GameClient {
    private String serverUrl;
    private ClientData clientData;

    public GameClient(String serverUrl, ClientData clientData) {
        this.serverUrl = serverUrl;
        this.clientData = clientData;
    }


    public String eval(String line) {
        return drawBoard();
    }

    public String drawBoard() {
        return BoardUi.drawBoard(clientData.getGame().getBoard(), clientData.getPlayerColor());
    }


}
