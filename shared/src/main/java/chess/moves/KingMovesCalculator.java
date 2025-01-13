package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class KingMovesCalculator implements PieceMoveCalculator {
    public static HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int[][] paths = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}};

        return  PieceMoveCalculator.calculateBoundMove(board, position, paths);
    }
}
