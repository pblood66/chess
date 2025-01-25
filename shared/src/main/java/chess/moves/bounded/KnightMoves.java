package chess.moves.bounded;

import chess.ChessBoard;
import chess.ChessPosition;

public class KnightMoves extends BoundedMoves {
    public KnightMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        this.pieceColor = board.getPiece(position).getTeamColor();
        this.PATHS = new int[][] {{1, 2}, {2, 1}, {-1, 2}, {-2, 1}, {1,-2}, {2, -1}, {-1,-2}, {-2,-1}};
    }
}
