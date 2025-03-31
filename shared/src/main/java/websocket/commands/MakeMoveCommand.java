package websocket.commands;

import chess.ChessMove;

import java.util.Objects;

public class MakeMoveCommand extends UserGameCommand{
    private final ChessMove move;

    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move) {
        super(commandType, authToken, gameID);
        this.move = move;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MakeMoveCommand)) {
            return false;
        }
        MakeMoveCommand that = (MakeMoveCommand) o;
        return getCommandType() == that.getCommandType() &&
                Objects.equals(getAuthToken(), that.getAuthToken()) &&
                Objects.equals(getGameID(), that.getGameID()) &&
                Objects.equals(move, that.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthToken(), getGameID(), getMove());
    }

    public ChessMove getMove() {
        return move;
    }
}
