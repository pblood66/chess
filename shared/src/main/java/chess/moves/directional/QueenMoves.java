package chess.moves.directional;

import chess.ChessBoard;
import chess.ChessPosition;

public class QueenMoves extends DirectionalMoves {
    public QueenMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        this.pieceColor = board.getPiece(position).getTeamColor();

        this.paths = new int[][] {{1,-1}, {-1, 1},  {1, 0}, {0,1}, {-1, 0}, {0, -1}, {1,1}, {-1, -1}};
    }
}
