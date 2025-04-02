package server.handlers.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class GameSessionManager {
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Session>> gameSessions;
    public GameSessionManager() {
        gameSessions = new ConcurrentHashMap<>();
    }

    public void removeSession(int gameID, String authToken) {
        ConcurrentHashMap<String, Session> sessions = gameSessions.get(gameID);
        if (sessions != null) {
            sessions.remove(authToken);
        }
    }

    public void addSession(int gameID, String authToken, Session session) {
        if (!gameSessions.containsKey(gameID)) {
            gameSessions.put(gameID, new ConcurrentHashMap<>());
        }
        gameSessions.get(gameID).put(authToken, session);
    }

    public void broadcast(int gameID, ServerMessage message, String... excludeAuthToken) throws IOException {
        ConcurrentHashMap<String, Session> sessions = gameSessions.get(gameID);
        if (sessions == null) return;

        HashSet<String> excludedTokens = new HashSet<>(Arrays.asList(excludeAuthToken));

        for (var entry : sessions.entrySet()) {
            String authToken = entry.getKey();
            Session session = entry.getValue();

            if (!excludedTokens.contains(authToken) && session.isOpen()) {
                session.getRemote().sendString(message.toJson());
            }
        }
    }


}
