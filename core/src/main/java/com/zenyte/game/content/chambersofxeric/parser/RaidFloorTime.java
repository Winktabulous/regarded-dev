package com.zenyte.game.content.chambersofxeric.parser;

public class RaidFloorTime
{
    private final String level;
    private final int time;

    public RaidFloorTime(String level, int time)
    {
        this.level = level;
        this.time = time;
    }

    public String getLevel() {
        return level;
    }

    public int getTime() {
        return time;
    }

}
