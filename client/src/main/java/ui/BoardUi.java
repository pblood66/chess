package ui;

import chess.*;

import static ui.EscapeSequences.*;

public class BoardUi {
    private static final String[] COLUMN_HEADERS = {"\u2003a ", "\u2003b ", "\u2003c ", "\u2003d ", "\u2003e ", "\u2003f ",
            "\u2003g ", "\u2003h "};


    public static String drawBoard(ChessBoard board, ChessGame.TeamColor playerColor, ChessMove... move) {
        StringBuilder builder = new StringBuilder();


        int startRow = (playerColor == ChessGame.TeamColor.WHITE) ? 0 : 7;
        int endRow = (playerColor == ChessGame.TeamColor.WHITE) ? 8 : -1;
        int step = (playerColor == ChessGame.TeamColor.WHITE) ? 1 : -1;

        builder.append(SET_TEXT_COLOR_BLUE);
        builder.append(getColumnHeaders(playerColor));

        for (int row = startRow; row != endRow; row += step) {
            builder.append(SET_BG_COLOR_DARK_GREY)
                    .append(" ").append(8 - row).append(" ")
                    .append(RESET_BG_COLOR);

            for (int col = startRow; col != endRow; col += step) {
                builder.append(setTile(col, row, board.getPiece(new ChessPosition(row + 1, col + 1))));
            }

            builder.append(SET_BG_COLOR_DARK_GREY)
                    .append(" ").append(8 - row).append(" ")
                    .append(RESET_BG_COLOR)
                    .append("\n");
        }

        builder.append(getColumnHeaders(playerColor));
        builder.append(RESET_TEXT_COLOR);

        return builder.toString();
    }

    private static String setTile(int row, int col, ChessPiece piece) {
        String tile = (row + col) % 2 == 0 ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK;
        // TODO: add highlight legal moves
        if (piece != null) {
            tile += getPieceString(piece.getPieceType(), piece.getTeamColor());
        } else {
            tile += EMPTY;
        }

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
