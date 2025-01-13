package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class KnightMovesCalculator {
    public static HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int [][] paths = {{1,2}, {2,1}, {2,-1}, {1, -2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}};

        HashSet<ChessMove> moves = PieceMoveCalculator.calculateBoundMove(board, position, paths);

        return moves;
    }
}
