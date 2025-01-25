package chess.moves.bounded;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.moves.MoveCalculator;

import java.util.HashSet;

public abstract class BoundedMoves extends MoveCalculator {
    public HashSet<ChessMove> pieceMoves() {
        HashSet<ChessMove> moves = new HashSet<>();

        for (int[] path : PATHS) {
            ChessMove potentialMove = calculateSingleMove(position, path);
            ChessPosition newPosition = potentialMove.getEndPosition();

            if (isOutOfBounds(newPosition)) {
                continue;
            }
            ChessPiece potentialPiece = board.getPiece(newPosition);
            if (potentialPiece == null) {
                moves.add(potentialMove);
            }
            else if (potentialPiece.getTeamColor() != pieceColor) {
                moves.add(potentialMove);
            }

        }

        return moves;
    }

}
