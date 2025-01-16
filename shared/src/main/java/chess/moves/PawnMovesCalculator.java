package chess.moves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class PawnMovesCalculator implements PieceMoveCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessGame.TeamColor pieceColor;
    private final int direction;



    PawnMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        this.pieceColor = board.getPiece(position).getTeamColor();

        if (this.pieceColor == ChessGame.TeamColor.BLACK) {
            direction = -1;
        }
        else {
            direction = 1;
        }
    }

    HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();

        // pawn moves 1 space unless it's on its starting space then it moves 2
        int [][] paths = {{0, direction}, {0, direction * 2}};

        // pawns attack pieces on its diagonal in the direction it's facing
        int [][] attackPath = {{-1, direction}, {1, direction}};

        HashSet<ChessMove> moves = new HashSet<>();

        // if pawn hasn't moved
        if (isOnStart()) {
            for (int [] path : paths) {
                moves.add(PieceMoveCalculator.calculateSingleMove(position, position, path));
            }
        }

        moves.add(attackPiece(attackPath[0]));

        // board, position, path, pieceColor,

        return moves;
    }

    boolean isOnStart() {
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            return position.getRow() == 2;
        }
        else if (pieceColor == ChessGame.TeamColor.BLACK) {
            return position.getRow() == 7;
        }
        return false;
    }

    boolean isOnEnd() {
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            return position.getRow() == 8;
        }
        else if (pieceColor == ChessGame.TeamColor.BLACK) {
            return position.getRow() == 1;
        }
        return false;
    }

    ChessMove attackPiece(int[] attackDirection) {
        HashSet<ChessMove> attackMoves = new HashSet<>();
        ChessPosition enemyPosition = new ChessPosition(position.getRow() + attackDirection[0],
                position.getColumn() + attackDirection[1]);

        // if the move is lands on an enemy, capture enemy
        if (board.getPiece(enemyPosition).getTeamColor() != pieceColor) {
            return new ChessMove(position, enemyPosition, null);
        }


        return null;
    }





}
