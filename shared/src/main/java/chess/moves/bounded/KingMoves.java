package chess.moves.bounded;

import chess.ChessBoard;
import chess.ChessPosition;

public class KingMoves extends BoundedMoves{
    public KingMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        this.pieceColor = board.getPiece(position).getTeamColor();
        this.PATHS = new int[][] {{1,1}, {1,-1}, {-1, 1}, {-1, -1}, {1, 0}, {0,1}, {-1, 0}, {0, -1}};
    }
}
