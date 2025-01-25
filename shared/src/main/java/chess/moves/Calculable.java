package chess.moves;

import chess.ChessMove;

import java.util.HashSet;

public interface Calculable {
    HashSet<ChessMove> pieceMoves();
}
