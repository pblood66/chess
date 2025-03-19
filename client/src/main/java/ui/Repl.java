package ui;

import static ui.EscapeSequences.*;

import java.util.Scanner;


public class Repl {
    private boolean loggedIn;
    private final PreLoginClient preLogin;

    public Repl(String serverUrl) {
        loggedIn = false;
        preLogin = new PreLoginClient(serverUrl);
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_BLUE + "♕ 240 Chess Client" + RESET_TEXT_COLOR);
        System.out.println(preLogin.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();



            try {
                if (!loggedIn) {
                    result = preLogin.eval(line);
                }
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}
