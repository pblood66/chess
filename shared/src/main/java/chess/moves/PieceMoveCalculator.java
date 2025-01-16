package chess.moves;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessPiece;

import java.util.HashSet;

public interface PieceMoveCalculator {
    static HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        return null;
    }

    static boolean moveIsInBounds(ChessPosition position) {
        if (position.getRow() > 8 || position.getRow() < 1) {
            return false;
        }
        return position.getColumn() <= 8 && position.getColumn() >= 1;
    }


    static HashSet<ChessMove> calculateBoundMove(ChessBoard board, ChessPosition position, int[][] paths) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();

        for (int[] path : paths) {
            ChessMove currentMove = calculateSingleMove(position, position, path);
            ChessPosition finalPosition = currentMove.getEndPosition();

            if (moveIsInBounds(finalPosition)) {
                if (board.getPiece(finalPosition) == null) {
                    possibleMoves.add(currentMove);
                }
                // if move is valid capture (different colors), add to set and continue
                else if (board.getPiece(finalPosition).getTeamColor() != pieceColor) {
                    possibleMoves.add(currentMove);
                }
            }
        }

        return possibleMoves;
    }

    static HashSet<ChessMove> calculateEntireDirection(ChessBoard board, ChessPosition position, int[] path) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        ChessPosition currentPosition = position;
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
        boolean canMove = true;

        while (canMove) {
            ChessMove currentMove = calculateSingleMove(currentPosition, position, path);
            currentPosition = currentMove.getEndPosition();

            // if move is outside the chessboard break and don't add move to set
            if (!moveIsInBounds(currentPosition)) {
                canMove = false;
            }
            // if space is empty add to set
            else if (board.getPiece(currentPosition) == null) {
                possibleMoves.add(currentMove);
            }
            // if move is valid capture (different colors), add to set and break
            else if (board.getPiece(currentPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(currentMove);
                canMove = false;
            }
            // if move is blocked by ally, break and don't add move to set
            else if (board.getPiece(currentPosition).getTeamColor() == pieceColor) {
                canMove = false;
            }
            else {
                canMove = false;
            }
        }

        return possibleMoves;
    }

    static ChessMove calculateSingleMove(ChessPosition currentPosition, ChessPosition initialPosition, int[] path) {
        int moveRow = currentPosition.getRow() + path[0];
        int moveCol = currentPosition.getColumn() + path[1];

        ChessPosition newPosition = new ChessPosition(moveRow, moveCol);

        return new ChessMove(initialPosition, newPosition, null);
    }

    static ChessMove calculateSingleMove(ChessPosition currentPosition, ChessPosition initialPosition, int[] path, ChessPiece.PieceType promotion) {
        int moveRow = currentPosition.getRow() + path[0];
        int moveCol = currentPosition.getColumn() + path[1];

        ChessPosition newPosition = new ChessPosition(moveRow, moveCol);

        return new ChessMove(initialPosition, newPosition, promotion);
    }
}
