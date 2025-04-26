package com.zenyte.game.content.chambersofxeric.parser;

public class RaidPlayer {
    String playerName;
    int points;

    public RaidPlayer(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPoints() {
        return points;
    }

    public RaidPlayer(String playerName, int points) {
        this.playerName = playerName;
        this.points = points;
    }

    @Override
    public String toString() {
        return "RaidPlayer(playerName=" + this.getPlayerName() + ", points=" + this.getPoints() + ")";
    }
}
