package ui.clients;

import chess.ChessGame;
import models.GameData;

import java.util.Collection;

public class ClientData {
    private String authToken;
    private String username = "";

    private ChessGame.TeamColor playerColor;
    private GameData currentGame;
    private Collection<GameData> games;

    private ClientState state;

    public ClientData() {
        this.authToken = "";
        this.state = ClientState.LOGGED_OUT;
    }

    public enum ClientState {
        LOGGED_OUT,
        LOGGED_IN,
        IN_GAME
    }


    public GameData getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(GameData currentGame) {
        this.currentGame = currentGame;
    }

    public Collection<GameData> getGames() {
        return games;
    }

    public void setGames(Collection<GameData> games) {
        this.games = games;
    }

    public ClientState getState() {
        return state;
    }

    public void setState(ClientState state) {
        this.state = state;
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
