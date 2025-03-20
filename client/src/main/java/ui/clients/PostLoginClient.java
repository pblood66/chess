package ui.clients;

import chess.ChessGame;
import models.results.CreateGameResult;
import models.results.ListGamesResult;
import ui.ServerFacade;

import java.util.Arrays;

public class PostLoginClient {
    private ServerFacade server;
    private ClientData clientData;

    public PostLoginClient(String serverUrl, ClientData clientData) {
        server = new ServerFacade(serverUrl);
        this.clientData = clientData;
    }

    public String eval(String line) {
        String[] tokens = line.split(" ");
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        switch (tokens[0]) {
            case "logout":
                logout(clientData.getAuthToken());
                return "Logged out";
            case "create":
                createGame(params, clientData.getAuthToken());
                return "Created game: " + params[0];
            case "list":
                var games = listGames(clientData.getAuthToken());
                return games.toString();
            case "join":
                joinGame(params, clientData.getAuthToken());

                return "Joined game: " + params[0];
            case "quit":
                return tokens[0];

            default:
                return help();
        }

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
            clientData.setAuthToken("");
            clientData.setUsername("");
            clientData.setState(ClientData.ClientState.LOGGED_OUT);
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
            clientData.setGameId(Integer.parseInt(params[0]));
            clientData.setPlayerColor(ChessGame.TeamColor.valueOf(params[1]));
            clientData.setState(ClientData.ClientState.IN_GAME);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
