package chess;
import java.util.HashSet;
import java.util.List;

public interface PieceMoveCalculator {
    static HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        return null;
    }

    static boolean moveIsInBounds(ChessPosition position) {
        if (position.getRow() <= 8 || position.getRow() >= 1) {
            return true;
        }
        else if (position.getColumn() <= 8 || position.getColumn() >= 1) {
            return true;
        }

        return false;
    }

    static ChessMove calculateSingleMove(ChessPosition position, int[] direction) {
        int moveRow = position.getRow() + direction[0];
        int moveCol = position.getColumn() + direction[1];

        ChessPosition newPosition = new ChessPosition(moveRow, moveCol);

        return new ChessMove(position, newPosition, null);
    }

//    static HashSet<ChessMove> calculateMovesDirection(ChessPosition position, int[] direction) {
//        do {
//
//        } while(moveIsInBounds(position));
//    }
}
