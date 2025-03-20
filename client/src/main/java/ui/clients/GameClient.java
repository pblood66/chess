package ui.clients;

import chess.ChessGame;
import models.GameData;

public class GameClient {
    private final String authToken;
    private final ChessGame game;
    private final int gameId;
    private final ChessGame.TeamColor playerColor;

    public GameClient(String authToken, ChessGame game, int gameID, ChessGame.TeamColor playerColor) {
        this.authToken = authToken;
        this.game = game;
        this.gameId = gameID;
        this.playerColor = playerColor;
    }


}
