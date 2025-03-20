package ui.clients;

import models.results.CreateGameResult;
import models.results.ListGamesResult;
import ui.ServerFacade;

public class PostLoginClient {
    private String serverUrl;
    private ServerFacade server;
    public PostLoginClient(String serverUrl) {
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
    }
    // contains functions for client to logout, create game, list games, play game, observe, and help
    public String help() {
        return """
                logout - logs user out
                create <NAME> - creates a new game
                list - lists all the games
                join <ID> [WHITE|BLACK] - joins the game
                observe - spectate a game
                quit - quits playing chess
                help - shows this help message
                """;
    }

    public void logout(String authToken) {
        try {
            server.logout(authToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CreateGameResult createGame(String[] params, String authToken) {
        try {
            return server.createGame(params[0], authToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ListGamesResult listGames(String authToken) {
        try {
            return server.listGames(authToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void joinGame(String[] params, String authToken) {
        try {
            server.joinGame(params[1],
                    Integer.parseInt(params[0]),
                    authToken);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
