package websocket.messages;

import chess.ChessGame;
import models.GameData;

public class LoadGameMessage extends ServerMessage {

    private final GameData game;
    private final ChessGame.TeamColor orientation;

    public LoadGameMessage(final GameData game, ChessGame.TeamColor orientation) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.orientation = orientation;
    }

    public LoadGameMessage(ServerMessageType type, final GameData game, ChessGame.TeamColor orientation) {
        super(type);
        this.game = game;
        this.orientation = orientation;
    }

    public GameData getGame() {
        return game;
    }

    public ChessGame.TeamColor getOrientation() {
        return orientation;
    }
}
