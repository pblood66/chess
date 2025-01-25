package chess;

import chess.moves.*;
import chess.moves.bounded.KingMoves;
import chess.moves.bounded.KnightMoves;
import chess.moves.directional.BishopMoves;
import chess.moves.directional.QueenMoves;
import chess.moves.directional.RookMoves;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            return String.valueOf(Character.toLowerCase(type.getCharValue()));
        }

        return String.valueOf(type.getCharValue());
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING('K'),
        QUEEN('Q'),
        BISHOP('B'),
        KNIGHT('N'),
        ROOK('R'),
        PAWN('P');

        private final char value;

        PieceType(char value) {
            this.value = value;
        }

        public char getCharValue() {
            return value;
        }

    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        MoveCalculator calculator = switch(type) {
            case BISHOP -> new BishopMoves(board, myPosition);
            case QUEEN -> new QueenMoves(board, myPosition);
            case ROOK -> new RookMoves(board, myPosition);
            case KING -> new KingMoves(board, myPosition);
            case KNIGHT -> new KnightMoves(board, myPosition);
            case PAWN -> new PawnMoves(board, myPosition);
        };

        return calculator.pieceMoves();
    }
}
