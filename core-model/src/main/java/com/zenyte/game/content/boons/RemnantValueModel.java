package com.zenyte.game.content.boons;

import java.util.ArrayList;

public class RemnantValueModel {
    private ArrayList<Integer> ids;
    private int value;

    public RemnantValueModel(int value, ArrayList<Integer> itemIds) {
        this.value = value;
        this.ids = itemIds;
    }

    public int getValue() {
        return value;
    }

    public ArrayList<Integer> getIds() {
        return this.ids;
    }

}
