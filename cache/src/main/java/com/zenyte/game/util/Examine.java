package com.zenyte.game.util;

public class Examine {

    private final int id;
    private final String examine;

    public Examine(int id, String examine) {
        this.id = id;
        this.examine = examine;
    }

    public int getId() {
        return id;
    }

    public String getExamine() {
        return examine;
    }
}
