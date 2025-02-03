package chess.moves.directional;

import chess.ChessBoard;
import chess.ChessPosition;

public class RookMoves extends DirectionalMoves {
    public RookMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        this.pieceColor = board.getPiece(position).getTeamColor();
        this.paths = new int[][] {{1, 0}, {0,1}, {-1, 0}, {0, -1}};
    }
}
