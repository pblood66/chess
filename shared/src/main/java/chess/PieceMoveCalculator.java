package chess;
import java.util.HashSet;

public interface PieceMoveCalculator {
    static HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        return null;
    }

    static boolean moveIsInBounds(ChessPosition position) {
        if (position.getRow() > 8 || position.getRow() < 1) {
            return false;
        }
        return position.getColumn() <= 8 && position.getColumn() >= 1;
    }


    static HashSet<ChessMove> calculateEntireDirection(ChessBoard board, ChessPosition position, int[] direction) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        ChessPosition currentPosition = position;
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
        boolean canMove = true;

        while (canMove) {
            ChessMove currentMove = calculateSingleMove(currentPosition, position, direction);
            currentPosition = currentMove.getEndPosition();

            // if move is outside the chessboard break and don't add move to set
            if (!moveIsInBounds(currentPosition)) {
                canMove = false;
            }
            // if space is empty add to set
            else if (board.getPiece(currentPosition) == null) {
                possibleMoves.add(currentMove);
            }
            // if move is valid capture (different colors), add to set and break
            else if (board.getPiece(currentPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(currentMove);
                canMove = false;
            }
            // if move is blocked by ally, break and don't add move to set
            else if (board.getPiece(currentPosition).getTeamColor() == pieceColor) {
                canMove = false;
            }
            else {
                canMove = false;
            }
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
