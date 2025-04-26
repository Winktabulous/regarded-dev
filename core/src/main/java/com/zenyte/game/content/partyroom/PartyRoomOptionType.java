package com.zenyte.game.content.partyroom;

/**
 * @author Kris | 02/07/2022
 */
public enum PartyRoomOptionType {
    ONE(1),
    FIVE(5),
    X(-1),
    ALL(Integer.MAX_VALUE),
    EXAMINE(-1);

    public int getAmount() {
        return amount;
    }

    private final int amount;
    PartyRoomOptionType(int amount) {
        this.amount = amount;
    }
}