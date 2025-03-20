package ui.clients;

import models.results.LoginResult;
import models.results.RegisterResult;
import ui.ServerFacade;

public class PreLoginClient {
    private ServerFacade server;
    private String serverUrl;

    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String help() {
        return """ 
                    login <username> <password> - logs user in to play chess
                    register <username> <password> <email> - registers user
                    help - shows this help message
                    quit - exits the program
                    """;
    }

    public String quit() {
        return "quit";
    }

    public RegisterResult register(String[] params) {
        try {
            return server.register(params[0], params[1], params[2]);
        } catch (Exception ex) {
            return null;
        }
    }

    public LoginResult login(String[] params) {
        try {
            return server.login(params[0], params[1]);
        } catch (Exception ex) {
            return null;
        }
    }


}
