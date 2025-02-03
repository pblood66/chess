package chess.moves;

import chess.*;

public abstract class MoveCalculator implements Calculable {
    protected ChessBoard board;
    protected ChessPosition position;
    protected ChessGame.TeamColor pieceColor;
    protected int[][] paths;

    // for all PieceType
    protected ChessMove calculateSingleMove(ChessPosition currPosition, int[] path) {
        int row = currPosition.getRow() + path[0];
        int col = currPosition.getColumn() + path[1];
        ChessPosition endPosition = new ChessPosition(row, col);


        return new ChessMove(position, endPosition, null);
    }

    // for pawns only
    protected ChessMove calculateSingleMove(ChessPosition currPosition, int[] path, ChessPiece.PieceType promotion) {
        int row = currPosition.getRow() + path[0];
        int col = currPosition.getColumn() + path[1];
        ChessPosition endPosition = new ChessPosition(row, col);

        return new ChessMove(position, endPosition, promotion);
    }

    public static boolean isOutOfBounds(ChessPosition newPosition) {
        int row = newPosition.getRow();
        int col = newPosition.getColumn();

        if (row > 8 || row < 1) {
            return true;
        }
        else {
            return col > 8 || col < 1;
        }
    }

}
