package chess.moves;

import chess.*;
import chess.ChessPiece.PieceType;

import java.util.HashSet;

public class PawnMoves extends MoveCalculator {
    private final int direction;

    public PawnMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        this.pieceColor = board.getPiece(position).getTeamColor();
        this.direction = (this.pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        this.PATHS = new int[][] {{this.direction, 0}, {this.direction * 2, 0}};
    }

    public HashSet<ChessMove> pieceMoves() {
        PieceType[] promotionType = (canPromote()) ?
                new PieceType[] {PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN}
                : new PieceType[] {null};

        // calculate column moves
        HashSet<ChessMove> moves = calculateColumn(promotionType);

        moves.addAll(calculateAttack(promotionType));

        return moves;
    }

    private HashSet<ChessMove> calculateColumn(PieceType[] promotionType) {
        HashSet<ChessMove> moves = new HashSet<>();

        if (isOnStart()) {
            for (int[] path : PATHS) {
                ChessMove potentialMove = calculateSingleMove(position, path);
                ChessPiece potentialPiece = board.getPiece(potentialMove.getEndPosition());

                if (potentialPiece == null) {
                    moves.add(potentialMove);
                }
                else break;
            }
        }
        else {
            for (PieceType promotion : promotionType) {
                ChessMove potentialMove = calculateSingleMove(position, PATHS[0], promotion);
                ChessPiece potentialPiece = board.getPiece(potentialMove.getEndPosition());

                if (potentialPiece == null) {
                    moves.add(potentialMove);
                }
                else {
                    break;
                }
            }
        }

        return moves;
    }

    private HashSet<ChessMove> calculateAttack(PieceType[] promotionType) {
        int[][] attacks = {{direction, 1}, {direction, -1}};
        HashSet<ChessMove> moves = new HashSet<>();

        for (int[] attack : attacks) {
            for (PieceType promotion : promotionType) {
                ChessMove potentialMove = calculateSingleMove(position, attack, promotion);
                ChessPosition newPosition = potentialMove.getEndPosition();

                if (isOutOfBounds(newPosition)) {
                    break;
                }
                ChessPiece potentialEnemy = board.getPiece(newPosition);
                if (potentialEnemy == null) {
                    break;
                }
                else if (potentialEnemy.getTeamColor() == pieceColor) {
                    break;
                }
                else {
                    moves.add(potentialMove);
                }
            }
        }

        return moves;
    }

    private boolean isOnStart() {
        if (pieceColor == ChessGame.TeamColor.WHITE && position.getRow() == 2) {
            return true;
        }
        else return pieceColor == ChessGame.TeamColor.BLACK && position.getRow() == 7;
    }

    private boolean canPromote() {
        if (pieceColor == ChessGame.TeamColor.WHITE && position.getRow() == 7) {
            return true;
        }
        else return pieceColor == ChessGame.TeamColor.BLACK && position.getRow() == 2;
    }

}
