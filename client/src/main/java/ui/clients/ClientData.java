package ui.clients;

import chess.ChessGame;

public class ClientData {
    private String authToken;
    private String username = "";

    private int gameId;
    private ChessGame.TeamColor playerColor;
    private ChessGame game;

    private ClientState state;

    public enum ClientState {
        LOGGED_OUT,
        LOGGED_IN,
        IN_GAME
    }

    public ClientData() {
        this.authToken = "";
        this.gameId = 0;
        this.state = ClientState.LOGGED_OUT;
        game = new ChessGame();
    }

    public ClientState getState() {
        return state;
    }

    public void setState(ClientState state) {
        this.state = state;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }
}
