package com.zenyte.game.content.boons;

public class BoonModel {
    public String name = "";
    public String description = "";
    public int price = 999999;
    public int itemId = 0;
    public int sort = 0;

    public BoonModel() {
    }

    public BoonModel(String name, String description, int price, int itemId, int sort) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.itemId = itemId;
        this.sort = sort;
    }

    public int getSort() {
        return sort;
    }
}
