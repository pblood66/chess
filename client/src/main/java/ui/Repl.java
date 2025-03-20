package ui;

import ui.clients.PostLoginClient;
import ui.clients.PreLoginClient;

import static ui.EscapeSequences.*;

import java.util.Arrays;
import java.util.Scanner;


public class Repl {
    private boolean loggedIn;
    private final PreLoginClient preLogin;
    private final PostLoginClient postLogin;
    private String authToken;
    private int gameId;

    public Repl(String serverUrl) {
        loggedIn = false;
        preLogin = new PreLoginClient(serverUrl);
        postLogin = new PostLoginClient(serverUrl);
        authToken = "";
        gameId = 0;
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_BLUE + "♕ 240 Chess Client" + RESET_TEXT_COLOR);
        System.out.println(preLogin.help());

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            if (!loggedIn) {
                result = preLoginEval(line);
            } else {
                if (gameId == 0) {
                    result = postLoginEval(line);
                }
                else {

                }
            }

            System.out.println(SET_TEXT_COLOR_BLUE + result);
        }
        scanner.close();
        System.out.println();
    }

    private String preLoginEval(String line) {
        String[] tokens = line.split(" ");
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        switch (tokens[0]) {
            case "register":
                var register = preLogin.register(params);
                authToken = register.authToken();
                loggedIn = true;
                return "Logged in as " + register.username();

            case "login":
                var login = preLogin.login(params);
                authToken = login.authToken();
                loggedIn = true;
                return "Logged in as " + login.username();
            case "quit":
                return preLogin.quit();
            default:
                return preLogin.help();
        }
    }

    private String postLoginEval(String line) {
        String[] tokens = line.split(" ");
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        switch (tokens[0]) {
            case "logout":
                postLogin.logout(authToken);
                loggedIn = false;
                return "Logged out";
            case "create":
                postLogin.createGame(params, authToken);
                return "Created game: " + params[0];
            case "list":
                var games = postLogin.listGames(authToken);
                return games.toString();
            case "join":
                postLogin.joinGame(params, authToken);
                gameId = Integer.parseInt(params[0]);

                return "Joined game: " + params[0];
            case "quit":
                return tokens[0];

            default:
                return postLogin.help();
        }
    }

    private void printPrompt() {
        if (loggedIn) {
            System.out.print("\n" + SET_TEXT_COLOR_GREEN+ "[LOGGED IN]"
                    + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN);
        }
        else {
            System.out.print("\n" + SET_TEXT_COLOR_RED + "[LOGGED OUT]"
                    + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN);
        }
    }
}
