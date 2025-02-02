package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {
    private ChessPiece[][] board = new ChessPiece[8][8];
    private HashMap<ChessPiece, ArrayList<ChessPosition>> pieces = new HashMap<>();

    public ChessBoard() {
        // populate HashMap with every pieceType and empty arrayList
        for (ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
            for (ChessGame.TeamColor teamColor : ChessGame.TeamColor.values()) {
                pieces.put(new ChessPiece(teamColor, pieceType), new ArrayList<ChessPosition>());
            }
        }
    }

    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();

            clone.board = new ChessPiece[8][8];
            clone.pieces = new HashMap<>();

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPiece piece = board[i][j];
                    if (piece != null) {
                        clone.board[i][j] = new ChessPiece(piece.getTeamColor(), piece.getPieceType());

                    }
                }
            }

            for (ChessPiece piece : pieces.keySet()) {
                ArrayList<ChessPosition> piecePositions = new ArrayList<>();
                piecePositions.addAll(pieces.get(piece));
                clone.pieces.put(piece, piecePositions);
            }

            return clone;

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getArrayRow()][position.getArrayCol()] = piece;
        pieces.get(piece).add(position);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getArrayRow()][position.getArrayCol()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // reset board to null
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }

        ChessGame.TeamColor[] teamColors = ChessGame.TeamColor.values();

        // black pawns row 7, white pawns row 2
        for (ChessGame.TeamColor team : teamColors) {
            addPawns(team);
            addBackRow(team);
        }


    }

    private void addPawns(ChessGame.TeamColor team) {
        int row = team == ChessGame.TeamColor.WHITE ? 2 : 7;

        for (int i =1; i <= 8; i++) {
            ChessPosition position = new ChessPosition(row, i);
            ChessPiece pawn = new ChessPiece(team, ChessPiece.PieceType.PAWN);

            addPiece(position, pawn);
        }
    }

    private void addBackRow(ChessGame.TeamColor team) {
        int row = team == ChessGame.TeamColor.WHITE ? 1 : 8;
        ChessPiece.PieceType[] backRow = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP};

        for (int i = 0; i < backRow.length; i++) {
            ChessPiece piece = new ChessPiece(team, backRow[i]);
            ChessPosition position = new ChessPosition(row, i + 1);

            addPiece(position, piece);
        }

        addPiece(new ChessPosition(row, 4), new ChessPiece(team, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(row, 5), new ChessPiece(team, ChessPiece.PieceType.KING));

        for (int i = backRow.length - 1; i >= 0; i--) {
            ChessPiece piece = new ChessPiece(team, backRow[i]);
            ChessPosition position = new ChessPosition(row, 8 - i);

            addPiece(position, piece);
        }

    }

    public void printBoard() {
        for (ChessPiece[] row : board) {
            for (ChessPiece piece : row) {
                System.out.print("|");
                if (piece != null) {
                    System.out.print(piece);
                }
                else {
                    System.out.print(" ");
                }
            }
                System.out.print("|");
                System.out.println();
        }
    }


}
