package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;


public class BishopMovesCalculator implements PieceMoveCalculator {
    public static HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();

        int[][] directions = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};

        for (int [] dir : directions) {
            HashSet<ChessMove> directionMoves = PieceMoveCalculator.calculateEntireDirection(board, position, dir);
            possibleMoves.addAll(directionMoves);
        }

        return possibleMoves;
    }

}