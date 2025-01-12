package chess;
import java.util.HashSet;
import java.util.List;

public interface PieceMoveCalculator {
    static HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        return null;
    }

    static boolean moveIsInBounds(ChessPosition position) {
        if (position.getRow() > 8 || position.getRow() < 1) {
            return false;
        }
        if (position.getColumn() > 8 || position.getColumn() < 1) {
            return false;
        }
        return true;
    }

    static HashSet<ChessMove> calculateEntireDirection(ChessBoard board, ChessPosition position, int[] direction) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        ChessPosition currentPosition = position;
        while (true) {
            ChessMove currentMove = calculateSingleMove(currentPosition, position, direction);
            currentPosition = currentMove.getEndPosition();

            // if move is outside the chessboard break from loop
            if (!moveIsInBounds(currentMove.getEndPosition())) {
                break;
            }
            possibleMoves.add(currentMove);
        }

        return possibleMoves;
    }

    static ChessMove calculateSingleMove(ChessPosition currentPosition, ChessPosition initialPosition, int[] direction) {
        int moveRow = currentPosition.getRow() + direction[0];
        int moveCol = currentPosition.getColumn() + direction[1];

        ChessPosition newPosition = new ChessPosition(moveRow, moveCol);

        return new ChessMove(initialPosition, newPosition, null);
    }
}
