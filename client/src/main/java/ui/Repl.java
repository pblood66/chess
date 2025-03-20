package ui;

import ui.clients.ClientData;
import ui.clients.GameClient;
import ui.clients.PostLoginClient;
import ui.clients.PreLoginClient;

import static ui.EscapeSequences.*;

import java.util.Scanner;


public class Repl {
    private final PreLoginClient preLogin;
    private final PostLoginClient postLogin;
    private GameClient game;

    private ClientData clientData;

    public Repl(String serverUrl) {
        clientData = new ClientData();
        preLogin = new PreLoginClient(serverUrl, clientData);
        postLogin = new PostLoginClient(serverUrl, clientData);
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_BLUE + "♕ 240 Chess Client" + RESET_TEXT_COLOR);
        System.out.println(preLogin.help());

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            result = line;

            switch (clientData.getState()) {
                case LOGGED_OUT:
                    result = preLogin.eval(line);
                case LOGGED_IN:
                    result = postLogin.eval(line);
                case IN_GAME:
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
