package chess;

import java.util.HashSet;


class BishopMovesCalculator implements PieceMoveCalculator {
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