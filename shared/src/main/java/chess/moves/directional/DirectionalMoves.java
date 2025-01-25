package chess.moves.directional;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.moves.MoveCalculator;

import java.util.Collection;
import java.util.HashSet;

public abstract class DirectionalMoves extends MoveCalculator {

    public HashSet<ChessMove> pieceMoves() {
        HashSet<ChessMove> moves = new HashSet<>();

        for (int[] path : PATHS) {
            moves.addAll(calculateDirection(path));
        }

        return moves;
    }

    private Collection<ChessMove> calculateDirection(int[] path) {
        HashSet<ChessMove> moves = new HashSet<>();
        boolean isValid = true;
        ChessPosition currentPosition = position;

        while(isValid) {
            ChessMove potentialMove = calculateSingleMove(currentPosition, path);
            currentPosition = potentialMove.getEndPosition();

            if (isOutOfBounds(currentPosition)) {
                break;
            }
            ChessPiece potentialPiece = board.getPiece(currentPosition);
            if (potentialPiece == null) {
                moves.add(potentialMove);
            }
            else if (potentialPiece.getTeamColor() != pieceColor) {
                moves.add(potentialMove);
                isValid = false;
            }
            else {
                isValid = false;
            }

        }

        return moves;
    }
}
