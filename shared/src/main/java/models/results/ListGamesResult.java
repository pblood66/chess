package models.results;

import models.GameData;

import java.util.Collection;

public record ListGamesResult(Collection<GameData> games) {
    public String toString() {
        StringBuilder result = new StringBuilder();
        int index = 1;
        for (GameData gameData : games) {
            String whiteUsername = gameData.whiteUsername() == null ? "" : gameData.whiteUsername();
            String blackUsername = gameData.blackUsername() == null ? "" : gameData.blackUsername();
            result.append(index).append(". ");
            result.append(gameData.gameName());
            result.append(" ID: ").append(gameData.gameID());
            result.append("\n   White ♕ Player: ").append(whiteUsername);
            result.append("\n   Black ♕ Player: ").append(blackUsername);
            result.append("\n");
            index++;
        }

        return result.toString();
    }
}
