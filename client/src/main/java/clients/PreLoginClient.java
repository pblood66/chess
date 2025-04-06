package clients;

import java.util.Arrays;

public class PreLoginClient {
    private ServerFacade server;
    private final ClientData clientData;

    public PreLoginClient(ServerFacade server, ClientData clientData) {
        this.server = server;
        this.clientData = clientData;
    }

    public String eval(String line) {
        String[] tokens = line.split(" ");
        String[] params = tokens.length > 1 ? Arrays.copyOfRange(tokens, 1, tokens.length) : new String[0];

        // try catch block here to intercept error throws and return error message
        try {
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
        catch (Exception e) {
            return "[ERROR]: " + e.getMessage();
        }


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

    public void register(String[] params) throws Exception {
        if (params.length != 3) {
            throw new Exception("<username> <password> <email>");
        }

        try {
             var response = server.register(params[0], params[1], params[2]);
             clientData.setState(ClientData.ClientState.LOGGED_IN);
             clientData.setUsername(response.username());
             clientData.setAuthToken(response.authToken());
        } catch (Exception ex) {
            // throw new exception with appropriate error messages
            throw new Exception("Could not register user: " + params[0]);
        }
    }

    public void login(String[] params) throws Exception{
        if (params.length != 2) {
            throw new Exception("<username> <password>");
        }

        try {
            var response = server.login(params[0], params[1]);
            clientData.setState(ClientData.ClientState.LOGGED_IN);
            clientData.setUsername(response.username());
            clientData.setAuthToken(response.authToken());
        } catch (Exception ex) {
            throw new Exception("Could not login user: " + params[0]);
        }
    }



}
