package clients;

import com.google.gson.Gson;
import ui.BoardUi;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import javax.websocket.*;
import java.net.URI;

import static ui.EscapeSequences.*;

public class WebSocketCommunicator extends Endpoint {
    private final Session session;
    private final ClientData clientData;


    public WebSocketCommunicator(String serverUrl, ClientData clientData) throws Exception {
        String url = serverUrl.replace("http", "ws");
        URI socketURI = new URI(url + "/ws");

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, socketURI);
        this.clientData = clientData;

        this.session.addMessageHandler((MessageHandler.Whole<String>) this::handleMessage);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void handleMessage(String message) {
        System.out.print(ERASE_LINE + '\r');
        if (message.contains("LOAD_GAME")) {
            LoadGameMessage loadGame = new Gson().fromJson(message, LoadGameMessage.class);
            System.out.println(BoardUi.drawBoard(loadGame.getGame().game().getBoard(), clientData.getPlayerColor(), null));
            System.out.print(SET_TEXT_COLOR_GREEN + "[IN GAME]" + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN);
        }
        else if (message.contains("NOTIFICATION")) {
            NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
            System.out.println(notification.getMessage());
            System.out.print(SET_TEXT_COLOR_GREEN + "[IN GAME]" + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN);
        }
        else if (message.contains("ERROR")) {
            ErrorMessage error = new Gson().fromJson(message, ErrorMessage.class);
            System.out.println(error.getErrorMessage());
            System.out.print(SET_TEXT_COLOR_GREEN + "[IN GAME]" + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN);
        }
    }

    public void resign(String authToken, int gameID) {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        sendMessage(command.toJson());
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }
}