package com.zenyte.game.content.boons;

import com.zenyte.game.item.Item;

import java.util.ArrayList;

public class RemnantValue {
    private ArrayList<Integer> ids = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private int value;
    public RemnantValue(int value, int... itemIds) {
        this.value = value;
        for(int iid: itemIds) {
            ids.add(iid);
            items.add(new Item(iid));
        }
    }

    public int getValue() {
        return value;
    }

    public ArrayList<Item> getItems() {
        return this.items;
    }

    public ArrayList<Integer> getIds() {
        return this.ids;
    }

}
