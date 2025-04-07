package ui;

import chess.*;

import java.util.Collection;
import java.util.List;

import static ui.EscapeSequences.*;

public class BoardUi {
    private static final String[] COLUMN_HEADERS = {"\u2003a ", "\u2003b ", "\u2003c ", "\u2003d ", "\u2003e ", "\u2003f ",
            "\u2003g ", "\u2003h "};

    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        System.out.println(drawBoard(board, ChessGame.TeamColor.WHITE, null));
        System.out.println(drawBoard(board, ChessGame.TeamColor.BLACK, null));

    }


    public static String drawBoard(ChessBoard board, ChessGame.TeamColor playerColor, List<ChessPosition> moves) {
        StringBuilder builder = new StringBuilder();
        int startRow = (playerColor == ChessGame.TeamColor.WHITE) ? 7 : 0;
        int endRow = (playerColor == ChessGame.TeamColor.WHITE) ? -1 : 8;
        int step = (playerColor == ChessGame.TeamColor.WHITE) ? -1 : 1;

        builder.append(SET_TEXT_COLOR_BLUE);
        builder.append(getColumnHeaders(playerColor));

        for (int row = startRow; row != endRow; row += step) {
            builder.append(SET_BG_COLOR_DARK_GREY)
                    .append(" ").append(row + 1).append(" ")
                    .append(RESET_BG_COLOR);

            for (int col = startRow; col != endRow; col += step) {
                ChessPosition position = new ChessPosition(row + 1, col + 1);
                builder.append(setTile(position, board.getPiece(position), moves));
            }

            builder.append(SET_BG_COLOR_DARK_GREY)
                    .append(" ").append(row + 1).append(" ")
                    .append(RESET_BG_COLOR)
                    .append("\n");
        }

        builder.append(getColumnHeaders(playerColor));
        builder.append(RESET_TEXT_COLOR);

        return builder.toString();
    }

    private static String setTile(ChessPosition position, ChessPiece piece, Collection<ChessPosition> highlights) {
        String tile;
        boolean isHighlighted = false;
        if (highlights != null && !highlights.isEmpty()) {
            isHighlighted = highlights.contains(position);
        }

        boolean isLightSquare = (position.getArrayRow() + position.getArrayCol()) % 2 == 0;

        if (isHighlighted) {
            tile = isLightSquare ? SET_BG_COLOR_GREEN : SET_BG_COLOR_DARK_GREEN;
        } else {
            tile = isLightSquare ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK;
        }

        tile += (piece != null)
                ? getPieceString(piece.getPieceType(), piece.getTeamColor())
                : EMPTY;

        tile += RESET_BG_COLOR;

        return tile;
    }



    private static String getColumnHeaders(ChessGame.TeamColor teamColor) {
        StringBuilder headers = new StringBuilder();
        headers.append(SET_BG_COLOR_DARK_GREY).append(SET_TEXT_BOLD).append("   ");

        if (teamColor == ChessGame.TeamColor.WHITE) {
            for (String columnHeader : COLUMN_HEADERS) {
                headers.append(columnHeader);
            }
        }
        else {
            for (int i = COLUMN_HEADERS.length - 1; i >= 0; i--) {
                headers.append(COLUMN_HEADERS[i]);
            }
        }
        headers.append("   ").append(RESET_BG_COLOR).append(RESET_TEXT_BOLD_FAINT);
        headers.append("\n");

        return headers.toString();
    }

}
