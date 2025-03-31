package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {

    private final ChessGame game;

    public LoadGameMessage(final ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public LoadGameMessage(ServerMessageType type, final ChessGame game) {
        super(type);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }
}
