package chess;

import java.util.ArrayList;
import java.util.HashSet;


class BishopMovesCalculator implements PieceMoveCalculator {
    public static HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> possibleMoves = new HashSet<ChessMove>();

        int currRow = position.getRow();
        int currCol = position.getColumn();

        int[][] directions = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};

        for (int [] dir : directions) {
            possibleMoves.add(PieceMoveCalculator.calculateSingleMove(position, dir));
        }

        return possibleMoves;
    }

}