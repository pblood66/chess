package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class RookMovesCalculator implements PieceMoveCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    static final int[][] Paths = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public RookMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public HashSet<ChessMove> pieceMoves() {
        HashSet<ChessMove> possibleMoves = new HashSet<>();

        //int[][] paths = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int [] path : Paths) {
            HashSet<ChessMove> directionMoves = PieceMoveCalculator.calculateEntireDirection(board, position, path);
            possibleMoves.addAll(directionMoves);
        }

        return possibleMoves;
    }
}
