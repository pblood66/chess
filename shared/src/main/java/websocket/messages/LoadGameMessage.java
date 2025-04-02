package websocket.messages;

import models.GameData;

public class LoadGameMessage extends ServerMessage {

    private final GameData game;

    public LoadGameMessage(final GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public LoadGameMessage(ServerMessageType type, final GameData game) {
        super(type);
        this.game = game;
    }

    public GameData getGame() {
        return game;
    }
}
