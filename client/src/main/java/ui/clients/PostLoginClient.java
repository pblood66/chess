package ui.clients;

import chess.ChessGame;
import models.results.CreateGameResult;
import models.results.ListGamesResult;
import ui.BoardUi;
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
        String[] params = tokens.length > 1 ? Arrays.copyOfRange(tokens, 1, tokens.length) : new String[0];

        try {
            switch (tokens[0]) {
                case "logout":
                    logout(clientData.getAuthToken());
                    return "Logged out";
                case "create":
                    createGame(params, clientData.getAuthToken());
                    return "Created game: " + params[0];
                case "list":
                    var games = listGames(clientData.getAuthToken());
                    clientData.setGames(games.games());
                    return games.toString();
                case "join":
                    joinGame(params, clientData.getAuthToken());
                    System.out.println(BoardUi.drawBoard(new ChessGame().getBoard(), ChessGame.TeamColor.WHITE));
                    System.out.println(BoardUi.drawBoard(new ChessGame().getBoard(), ChessGame.TeamColor.BLACK));

                    return "Joined game: " + params[0];
                case "observe":
                    return observe(params);

                case "quit":
                    return tokens[0];

                default:
                    return help();
            }
        }
        catch (Exception e) {
            return "[ERROR]: " + e.getMessage();
        }

    }



    // contains functions for client to logout, create game, list games, play game, observe, and help
    public String help() {
        return """
                logout - logs user out
                create <NAME> - creates a new game
                list - lists all the games
                join <Game ID> [WHITE|BLACK] - joins the game
                observe <Game ID> - spectate a game
                quit - quits playing chess
                help - shows this help message
                """;
    }

    private void logout(String authToken) throws Exception {
        try {
            server.logout(authToken);
            clientData.setAuthToken("");
            clientData.setUsername("");
            clientData.setState(ClientData.ClientState.LOGGED_OUT);
        } catch (Exception e) {
            throw new Exception("Could not logout");
        }
    }

    private CreateGameResult createGame(String[] params, String authToken) throws Exception {
        if (params.length != 1) {
            throw new Exception("<Game Name>");
        }

        try {
            return server.createGame(params[0], authToken);
        } catch (Exception e) {
            throw new Exception("Could not create game: " + params[0]);
        }
    }

    private ListGamesResult listGames(String authToken) throws Exception {
        try {
            return server.listGames(authToken);
        } catch (Exception e) {
            throw new Exception("Could not list games");
        }
    }

    private void joinGame(String[] params, String authToken) throws Exception {
        if (params.length != 2) {
            throw new Exception("<Game ID> <WHITE/BLACK>");
        }

        try {
            server.joinGame(params[1],
                    Integer.parseInt(params[0]),
                    authToken);
            clientData.setGameId(Integer.parseInt(params[0]));
            clientData.setPlayerColor(ChessGame.TeamColor.valueOf(params[1]));
            clientData.setState(ClientData.ClientState.IN_GAME);
        } catch(Exception e) {
            throw new Exception("Could not join game: " + params[0]);
        }
    }

    private String observe(String[] params) throws Exception {
        var games = server.listGames(clientData.getAuthToken()).games();

        int gameId = Integer.parseInt(params[0]);

        for (var game : games) {
            if (game.gameID() == gameId) {

                String observe = "Observing Game: " + gameId + "\n" +
                        BoardUi.drawBoard(game.game().getBoard(), ChessGame.TeamColor.WHITE) +
                        "\n" +
                        BoardUi.drawBoard(game.game().getBoard(), ChessGame.TeamColor.BLACK);
                clientData.setState(ClientData.ClientState.IN_GAME);
                clientData.setGame(game.game());
                clientData.setGameId(gameId);

                return observe;
            }
        }

        throw new Exception("Could not find Game: " + gameId);
    }


}
