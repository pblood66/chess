package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class QueenMovesCalculator implements PieceMoveCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    static final int[][] PATHS = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    public QueenMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public HashSet<ChessMove> pieceMoves() {
        HashSet<ChessMove> possibleMoves = new HashSet<>();

        for (int [] path : PATHS) {
            HashSet<ChessMove> directionMoves = PieceMoveCalculator.calculateEntireDirection(board, position, path);
            possibleMoves.addAll(directionMoves);
        }

        return possibleMoves;
    }
}
