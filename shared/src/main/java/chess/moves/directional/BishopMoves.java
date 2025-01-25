package chess.moves.directional;

import chess.ChessBoard;
import chess.ChessPosition;


public class BishopMoves extends DirectionalMoves{

    public BishopMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        this.pieceColor = board.getPiece(position).getTeamColor();
        this.PATHS = new int[][]{{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};
    }
}
