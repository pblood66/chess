package models.requests;

public record CreateGameRequest(String authToken, String gameName) {
    public CreateGameRequest setAuthToken(String authToken) {
        return new CreateGameRequest(authToken, gameName);
    }
}
