package com.zenyte.game.world.entity.player;

/**
 * @author Tommeh | 4-2-2019 | 22:13
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum MessageType {

    UNFILTERABLE(0),
    GLOBAL_BROADCAST(14),
    EXAMINE_ITEM(27),
    EXAMINE_NPC(28),
    EXAMINE_OBJECT(29),
    CLAN(43),
    AUTOTYPER(90),
    TRADE_REQUEST(101),
    CHALLENGE_REQUEST(103),
    GAMBLE_REQUEST(104),
    FILTERABLE(105),
    MIDDLE_MAN_TRADE_REQUEST(106),
    CLAN_GIM_FORM_GROUP(111),
    CLAN_GIM_GROUP_WITH(112);

    private final int type;

    MessageType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
