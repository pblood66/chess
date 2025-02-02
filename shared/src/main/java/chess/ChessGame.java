package chess;

import chess.moves.MoveCalculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece currentPiece = board.getPiece(startPosition);
        if (currentPiece == null) {
            return null;
        }
        else {
            return currentPiece.pieceMoves(board, startPosition);
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);

        if (enemyCanCapture(this.board, teamColor, kingPosition)) {
            return true;
        }

        return false;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);
        int[][] relativePositions = {{1, 0}, {0 ,1}, {-1, 0}, {0, -1}, {1, 1}, {-1, 1}, {1, -1}, {-1, -1}};

        // For pawn valid moves, copy a new board and place kings in all the relative positions
        ChessBoard kingBoard = makeKingBoard(board.clone(), kingPosition, relativePositions);
        // Bug: Directional pieces like rook & Queen get messed up by the kingBoard
        // filter if Knight, King, or Pawn then use kingBoard


        board.printBoard();
        System.out.println();
        kingBoard.printBoard();

        // king is in checkmate if positions surrounding king and king position have valid moves
        if (!isInCheck(teamColor)) {
            return false;
        }
        // get threatening piece
        // if allied pieces can capture threatening piece then not checkmate


        for (int[] pos : relativePositions) {
            ChessPosition currentPosition = new ChessPosition(kingPosition.getRow() + pos[0],
                    kingPosition.getColumn() + pos[1]);
            // need to check if currentPosition is in bounds of the boards
            if (MoveCalculator.isOutOfBounds(currentPosition)) {
                continue;
            }

            if (!enemyCanCapture(kingBoard, teamColor, currentPosition)) {
                if (board.getPiece(currentPosition) != null) {
                    continue;
                }

                return false;
            }

        }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    // private ChessPosition getThreateningPiece();


    private ChessBoard makeKingBoard(ChessBoard board, ChessPosition kingPosition,
                               int[][] relativePositions) {
        TeamColor kingTeam = board.getPiece(kingPosition).getTeamColor();
        for (int[] pos : relativePositions) {
            int row = kingPosition.getRow() + pos[0];
            int col = kingPosition.getColumn() + pos[1];
            ChessPosition newPosition = new ChessPosition(row, col);

            if (MoveCalculator.isOutOfBounds(newPosition) || board.getPiece(newPosition) != null) {
                continue;
            }

            board.addPiece(newPosition, new ChessPiece(kingTeam, ChessPiece.PieceType.KING));
        }

        return board;
    }


    /**
     * Finds where on the board a specified king is
     * @param teamColor - which color of king to look for
     * @return the position of the specified king color on the board
     */
    private ChessPosition getKingPosition(TeamColor teamColor) {
        ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);

        ArrayList<ChessPosition> positions = board.getPositionArray(king);

        return positions.getLast();
    }

    /**
     * Determines if a given space could be captured by an enemy piece
     * @param teamColor - which team to check
     * @param position - chess position to check
     * @return True if the specified position can be captured by an enemy piece
     */
    private boolean enemyCanCapture(ChessBoard board, TeamColor teamColor, ChessPosition position) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                if (currentPiece != null && currentPiece.getTeamColor() != teamColor) {
                    ChessPiece.PieceType pieceType = currentPiece.getPieceType();
                    Collection<ChessMove> moves;

                    // if currentPiece is a bounded piece, use the kingBoard to check if a piece can capture the king
                    // else use the current board
                    if (pieceType == ChessPiece.PieceType.KING || pieceType == ChessPiece.PieceType.KNIGHT ||
                            pieceType == ChessPiece.PieceType.PAWN) {
                        moves = currentPiece.pieceMoves(board, currentPosition);
                    } else {
                        moves = currentPiece.pieceMoves(this.board, currentPosition);
                    }

                    for (ChessMove move : moves) {
                        ChessPosition endPosition = move.getEndPosition();
                        if (endPosition.equals(position)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
