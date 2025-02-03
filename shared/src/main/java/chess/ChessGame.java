package chess;

import chess.moves.MoveCalculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

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

        ChessBoard copy = board.clone();
        Collection<ChessMove> possibleMoves = currentPiece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();

        for (ChessMove move : possibleMoves) {
            board.addPiece(move.getEndPosition(), currentPiece);
            board.addPiece(move.getStartPosition(), null);

            if (!isInCheck(currentPiece.getTeamColor())) {
                validMoves.add(move);
            }
            board.addPiece(move.getEndPosition(), copy.getPiece(move.getEndPosition()));
            board.addPiece(startPosition, copy.getPiece(startPosition));
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece currentPiece = board.getPiece(move.getStartPosition());

        // start position must have a valid piece
        if (currentPiece == null) {
            throw new InvalidMoveException("No piece found for " + move.getStartPosition());
        }
        // currentPiece must match the game turn
        TeamColor pieceColor = currentPiece.getTeamColor();
        if (getTeamTurn() != pieceColor) {
            throw new InvalidMoveException("Team turn does not match");
        }

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        // move must be a valid move
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move " + move);
        }
        // if move can promote piece, promote currentPiece
        if (move.getPromotionPiece() != null) {
            board.addPiece(move.getEndPosition(), new ChessPiece(pieceColor, move.getPromotionPiece()));
        }
        else {
            board.addPiece(move.getEndPosition(), currentPiece);
        }
        board.addPiece(move.getStartPosition(), null);

        TeamColor teamTurn = (pieceColor == TeamColor.BLACK) ? TeamColor.WHITE : TeamColor.BLACK;
        setTeamTurn(teamTurn);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);

        return enemyCanCapture(teamColor, kingPosition);
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
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

    private boolean enemyCanCapture(TeamColor teamColor, ChessPosition position) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece currPiece = board.getPiece(new ChessPosition(i, j));
                if (currPiece == null || currPiece.getTeamColor() == teamColor) {
                    continue;
                }
                for (ChessMove move : currPiece.pieceMoves(board, new ChessPosition(i, j))) {
                    if (move.getEndPosition().equals(position)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition getKingPosition(TeamColor teamColor) {
        System.out.println();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece currPiece = board.getPiece(new ChessPosition(i, j));

                if (currPiece != null && teamColor == currPiece.getTeamColor()) {
                    if (currPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        return new ChessPosition(i, j);
                    }
                }
            }
        }
        System.out.println();
        return null;
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
