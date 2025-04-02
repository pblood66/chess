package clients;

import javax.websocket.*;
import java.net.URI;

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
                System.out.println(message);
            }
        });
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }
}