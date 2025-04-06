package ui;

import clients.*;

import java.util.Scanner;

import static ui.EscapeSequences.*;


public class Repl {
    private final PreLoginClient preLogin;
    private final PostLoginClient postLogin;
    private final GameClient gameClient;

    private final ClientData clientData;
    private final ServerFacade server;

    public Repl(String serverUrl) {
        clientData = new ClientData(serverUrl);
        this.server = new ServerFacade(serverUrl);
        preLogin = new PreLoginClient(server, clientData);
        postLogin = new PostLoginClient(server, clientData);
        gameClient = new GameClient(server, clientData);
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_BLUE + "♕ 240 Chess Client" + RESET_TEXT_COLOR);
        System.out.println(preLogin.help());

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            switch (clientData.getState()) {
                case LOGGED_OUT:
                    result = preLogin.eval(line);
                    break;
                case LOGGED_IN:
                    result = postLogin.eval(line);
                    break;
                case IN_GAME:
                    result = gameClient.eval(line);
                    break;
            }

            System.out.println(result);
        }

        scanner.close();
    }

    private String getClientState() {
        return switch(clientData.getState()) {
            case LOGGED_OUT -> SET_TEXT_COLOR_RED + "[LOGGED OUT]";
            case LOGGED_IN -> SET_TEXT_COLOR_BLUE + "[LOGGED IN]";
            case IN_GAME -> SET_TEXT_COLOR_GREEN + "[IN GAME]";
        };
    }

    private void printPrompt() {

        System.out.print(getClientState() + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN);

    }
}
