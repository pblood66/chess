package ui;

import java.util.Arrays;
import static ui.EscapeSequences.*;

public class PreLoginClient {
    private ServerFacade server;
    private String serverUrl;

    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    // contains functions for client to log in, register, help, and quit
    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = tokens.length > 0 ? tokens[0].toLowerCase() : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        return switch(cmd) {
            case "login" -> login(params);
            case "register" -> register(params);
            case "quit" -> quit();
            default -> help();
        };
    }

    public String help() {
        return """ 
                    login <username> <password> - logs user in to play chess
                    register <username> <password> <email> - registers user
                    help - brings up this help screen
                    quit - exits the program
                    """;
    }

    private String quit() {
        return "quit";
    }

    private String register(String[] params) {
        String username = params[0];
        String password = params[1];
        String email = params[2];

        try {
            server.register(username, password, email);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "register";
    }

    private String login(String[] params) {
        try {
            server.login(params[0], params[1]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "login";
    }


}
