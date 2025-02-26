package service.requests;

import chess.ChessGame;

public record JoinGameRequest(String authToken, String playerColor, int gameID) {
    public JoinGameRequest setAuthToken(String authToken) {
        return new JoinGameRequest(authToken, playerColor, gameID);
    }
}
