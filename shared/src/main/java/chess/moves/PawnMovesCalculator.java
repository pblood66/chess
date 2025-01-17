package chess.moves;

import chess.*;
import chess.ChessGame.TeamColor;

import java.util.HashSet;

public class PawnMovesCalculator implements PieceMoveCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final TeamColor pieceColor;
    private final int direction;



    public PawnMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        this.pieceColor = board.getPiece(position).getTeamColor();

        if (this.pieceColor == TeamColor.BLACK) {
            direction = -1;
        }
        else {
            direction = 1;
        }
    }

    public HashSet<ChessMove> pieceMoves() {
        // pawn moves 1 space unless it's on its starting space then it moves 2
        int [][] paths = {{direction, 0}, {direction * 2, 0}};
        // pawns attack pieces on its diagonal in the direction it's facing
        int [][] attackPath = {{direction, -1}, {direction, 1}};
        HashSet<ChessMove> moves = new HashSet<>();

        // if pawn is on second to last row, it can promote in next move
        ChessPiece.PieceType[] promotionTypes = {null};
        if (canPromote()) {
            promotionTypes = new ChessPiece.PieceType[] {ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};
        }

        int currRow = position.getRow();
        int currCol = position.getColumn();
        // calculate moves in pawn's column
        if (isOnStart()) {
            for (int[] path : paths) {
                ChessPosition potMove = new ChessPosition(currRow + path[0], currCol + path[1]);
                if (board.getPiece(potMove) != null) {
                    break;
                }
                moves.add(PieceMoveCalculator.calculateSingleMove(position, position, path));
            }
        }
        else {
            ChessPosition potMove = new ChessPosition(currRow + paths[0][0], currCol + paths[0][1]);
            if (board.getPiece(potMove) == null) {
                for (ChessPiece.PieceType promotion : promotionTypes) {
                    moves.add(PieceMoveCalculator.calculateSingleMove(position, position, paths[0], promotion));
                }
            }
        }

        //  calculate attack moves
        for (ChessPiece.PieceType promotion : promotionTypes) {
            for (int[] attack : attackPath) {
                // check if there is an enemy
                // add move if there is
                ChessPosition enemy = new ChessPosition(currRow + attack[0], currCol + attack[1]);
                if (PieceMoveCalculator.moveIsInBounds(enemy)) {
                    if (board.getPiece(enemy) != null && board.getPiece(enemy).getTeamColor() != pieceColor) {
                        moves.add(PieceMoveCalculator.calculateSingleMove(position, position, attack, promotion));
                    }
                }

            }
        }

        return moves;
    }

    private boolean isOnStart() {
        if (pieceColor == TeamColor.WHITE) {
            return position.getRow() == 2;
        }
        else if (pieceColor == TeamColor.BLACK) {
            return position.getRow() == 7;
        }
        return false;
    }

    private HashSet<ChessMove> calculateStartMoves(int[][] paths) {
        HashSet<ChessMove> moves = new HashSet<>();
        if (isBlocked(paths[0])) return moves;
        else {
            moves.add(PieceMoveCalculator.calculateSingleMove(position, position, paths[0]));
        }

        if (isBlocked(paths[1])) return moves;
        else {
            moves.add(PieceMoveCalculator.calculateSingleMove(position, position, paths[1]));
        }

        return moves;
    }

    boolean canPromote() {
        if (pieceColor == TeamColor.WHITE) {
            return position.getRow() == 7;
        }
        else if (pieceColor == TeamColor.BLACK) {
            return position.getRow() == 2;
        }
        return false;
    }

    private boolean isEnemy(int[] attackDirection) {
        ChessPosition enemyPosition = new ChessPosition(position.getRow() + attackDirection[0],
                position.getColumn() + attackDirection[1]);

        return board.getPiece(enemyPosition).getTeamColor() != pieceColor;
    }

    ChessMove attackPiece(int[] attackDirection) {
        ChessPosition enemyPosition = new ChessPosition(position.getRow() + attackDirection[0],
                position.getColumn() + attackDirection[1]);

        if (board.getPiece(enemyPosition) == null) return null;

        // if the move is lands on an enemy, capture enemy
        if (board.getPiece(enemyPosition).getTeamColor() != pieceColor) {
            return new ChessMove(position, enemyPosition, null);
        }

        return null;
    }

    boolean isBlocked(int[] path) {
        ChessPosition checkPosition = new ChessPosition(position.getRow() + path[0],
                position.getColumn() + path[1]);

        return !(board.getPiece(checkPosition) == null);
    }





}
