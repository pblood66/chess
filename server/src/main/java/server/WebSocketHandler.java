package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebSocketHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        System.out.println(command.getCommandType());
        switch (command.getCommandType()) {
            case CONNECT:
                break;
            case MAKE_MOVE:
                break;
            case RESIGN:
                break;
            case LEAVE:
                break;
        }
    }
}
