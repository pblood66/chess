package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class KingMovesCalculator implements PieceMoveCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    static final int[][] paths = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    public KingMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public HashSet<ChessMove> pieceMoves() {
        return  PieceMoveCalculator.calculateBoundMove(board, position, paths);
    }
}
