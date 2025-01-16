package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class KnightMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    static final int[][] paths = {{1,2}, {2,1}, {2,-1}, {1, -2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}};

    public KnightMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public HashSet<ChessMove> pieceMoves() {
        return PieceMoveCalculator.calculateBoundMove(board, position, paths);
    }
}
