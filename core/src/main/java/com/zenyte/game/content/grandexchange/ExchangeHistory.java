package com.zenyte.game.content.grandexchange;

/**
 * @author Tommeh | 18 sep. 2018 | 17:18:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ExchangeHistory {

    private final ExchangeType type;
    private final int id;
    private final int quantity;
    private final int price;

    public ExchangeHistory(int id, int quantity, int price, ExchangeType type) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public ExchangeType getType() {
        return type;
    }

}
