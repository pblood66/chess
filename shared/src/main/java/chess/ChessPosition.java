package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public ChessPosition(String position) {
        if (position.length() != 2) {
            throw new IllegalArgumentException("Invalid position format.");
        }

        char colChar = Character.toLowerCase(position.charAt(0));
        char rowChar = position.charAt(1);

        // Ensure first char is letter a-h and second is digit 1-8
        if (colChar < 'a' || colChar > 'h' || rowChar < '1' || rowChar > '8') {
            throw new IllegalArgumentException("Invalid column or row character.");
        }

        this.row =  8 - (rowChar - '1');
        this.col = colChar - 'a' + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "{" +
                row + ", " +
                col +
                '}';
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    // avoids out of bounds exception with array [0-7]
    public int getArrayRow() { return row - 1; }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    // to avoid out of bounds exception with array [0-7]
    public int getArrayCol() { return col - 1; }
}
