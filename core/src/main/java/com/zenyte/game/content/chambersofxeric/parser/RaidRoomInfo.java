package com.zenyte.game.content.chambersofxeric.parser;

public class RaidRoomInfo {
    private String label;
    private final String type;
    private final int index;
    private final int rotation;
    private final int chunkx;
    private final int chunky;
    private final int level;
    private int startTime;
    private int endTime;

    public RaidRoomInfo(String type, int index, int rotation, int chunkx,
                        int chunky, int level)
    {
        this.type = type;
        this.index = index;
        this.rotation = rotation;
        this.chunkx = chunkx;
        this.chunky = chunky;
        this.level = level;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public int getRotation() {
        return rotation;
    }

    public int getChunkx() {
        return chunkx;
    }

    public int getChunky() {
        return chunky;
    }

    public int getLevel() {
        return level;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }
}
