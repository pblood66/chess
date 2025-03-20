package ui.clients;

import models.results.LoginResult;
import models.results.RegisterResult;
import ui.ServerFacade;

import java.util.Arrays;

public class PreLoginClient {
    private ServerFacade server;
    private String serverUrl;
    private ClientData clientData;

    public PreLoginClient(String serverUrl, ClientData clientData) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.clientData = clientData;
    }

    public String eval(String line) {
        String[] tokens = line.split(" ");
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        return switch (tokens[0]) {
            case "register" -> {
                register(params);
                yield "Logged in as " + clientData.getUsername();
            }
            case "login" -> {
                login(params);
                yield "Logged in as " + clientData.getUsername();
            }
            case "quit" -> quit();
            default -> help();
        };


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

    public void register(String[] params) {
        try {
             var response = server.register(params[0], params[1], params[2]);
             clientData.setState(ClientData.ClientState.LOGGED_IN);
             clientData.setUsername(response.username());
             clientData.setAuthToken(response.authToken());
        } catch (Exception ex) {

        }
    }

    public void login(String[] params) {
        try {
            var response = server.login(params[0], params[1]);
            clientData.setState(ClientData.ClientState.LOGGED_IN);
            clientData.setUsername(response.username());
            clientData.setAuthToken(response.authToken());
        } catch (Exception ex) {

        }
    }



}
