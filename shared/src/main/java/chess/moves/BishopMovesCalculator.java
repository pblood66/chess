package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;


public class BishopMovesCalculator implements PieceMoveCalculator {
    public static HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();

        int[][] paths = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};

        for (int [] path : paths) {
            HashSet<ChessMove> directionMoves = PieceMoveCalculator.calculateEntireDirection(board, position, path);
            possibleMoves.addAll(directionMoves);
        }

        return possibleMoves;
    }

}