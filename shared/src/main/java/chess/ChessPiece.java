package chess;

import chess.moves.*;

import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import java.util.Collection;
import java.util.HashSet;
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

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
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

        Collection<ChessMove> moves = new HashSet<>();

        if (type.equals(ChessPiece.PieceType.PAWN)) {
            PawnMovesCalculator calculator = new PawnMovesCalculator(board, myPosition);
            moves = calculator.pieceMoves();
        }
        else if (type.equals(PieceType.BISHOP)) {
            BishopMovesCalculator calculator = new BishopMovesCalculator(board, myPosition);
            moves = calculator.pieceMoves();
        } else if (type.equals(PieceType.ROOK)) {
            RookMovesCalculator calculator = new RookMovesCalculator(board, myPosition);
            moves = calculator.pieceMoves();
        } else if (type.equals(PieceType.QUEEN)) {
            QueenMovesCalculator calculator = new QueenMovesCalculator(board, myPosition);
            moves = calculator.pieceMoves();
        } else if (type.equals(PieceType.KING)) {
            KingMovesCalculator calculator = new KingMovesCalculator(board, myPosition);
            moves = calculator.pieceMoves();
        } else if (type.equals(PieceType.KNIGHT)) {
            KnightMovesCalculator calculator = new KnightMovesCalculator(board, myPosition);
            moves = calculator.pieceMoves();
        }

        return moves;


    }
}
