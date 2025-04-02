package websocket.commands;

import chess.ChessGame;

public class ConnectCommand extends UserGameCommand {
    private final boolean isPlayer;
    private final ChessGame.TeamColor playerColor;

    public ConnectCommand(String authToken, Integer gameID, boolean isPlayer, ChessGame.TeamColor playerColor) {
        super(CommandType.CONNECT, authToken, gameID);
        this.isPlayer = isPlayer;
        this.playerColor = playerColor;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public boolean isPlayer() {
        return isPlayer;
    }
}
