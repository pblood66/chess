package clients;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.BoardUi;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

import static ui.EscapeSequences.*;

public class WebSocketCommunicator extends Endpoint {
    private Session session;


    public WebSocketCommunicator(String serverUrl) throws Exception {
        String url = serverUrl.replace("http", "ws");
        URI socketURI = new URI(url + "/ws");

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, socketURI);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                handleMessage(message);
            }
        });
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void handleMessage(String message) {
        ServerMessage serverMessage;
        System.out.print(ERASE_LINE + '\r');
        if (message.contains("LOAD_GAME")) {
            LoadGameMessage loadGame = new Gson().fromJson(message, LoadGameMessage.class);
            System.out.println(BoardUi.drawBoard(loadGame.getGame().game().getBoard(), loadGame.getOrientation()));
            System.out.print(SET_TEXT_COLOR_GREEN + "[IN GAME]" + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN);
        }
        else if (message.contains("NOTIFICATION")) {
            NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
            System.out.println(notification.getMessage());
            System.out.print(SET_TEXT_COLOR_GREEN + "[IN GAME]" + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN);
        }
        else if (message.contains("ERROR")) {
            ErrorMessage error = new Gson().fromJson(message, ErrorMessage.class);
            System.out.print(SET_TEXT_COLOR_GREEN + "[IN GAME]" + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN);
        }
    }

    public void leave(String authToken, int gameID) {
        UserGameCommand leave = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        sendMessage(leave.toJson());
    }

    public void resign(String authToken, int gameID) {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        sendMessage(command.toJson());
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }
}