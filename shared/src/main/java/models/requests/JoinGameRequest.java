package models.requests;

public record JoinGameRequest(String authToken, String playerColor, int gameID) {
    public JoinGameRequest setAuthToken(String authToken) {
        return new JoinGameRequest(authToken, playerColor, gameID);
    }
}
