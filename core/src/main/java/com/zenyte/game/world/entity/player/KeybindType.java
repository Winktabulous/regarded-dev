package com.zenyte.game.world.entity.player;

public enum KeybindType {

	ATTACK_STYLE_TAB(9, 4675, 1),
	SKILL_TAB(16, 4676, 2),
	QUEST_TAB(23, 4677, 3),
	INVENTORY_TAB(30, 4678, 13),
	EQUIPMENT_TAB(37, 4679, 4),
    PRAYER_TAB(44, 4680, 5),
    MAGIC_TAB(51, 4682, 6),
    CLAN_CHAT_TAB(93, 4683, 7),
    FRIENDS_TAB(58, 4684, 8),
    IGNORE_TAB(65, 6517, 9),
    OPTIONS_TAB(79, 4686, 10),
    EMOTE_TAB(86, 4687, 11),
    MUSIC_TAB(100, 4688, 12),
    LOGOUT_TAB(72, 4689, 0);

    public static final KeybindType[] VALUES = values();
    private final int componentId;
    private final int varbitId;
    private final int defaultValue;

    KeybindType(int componentId, int varbitId, int defaultValue) {
        this.componentId = componentId;
        this.varbitId = varbitId;
        this.defaultValue = defaultValue;
    }

    public int getComponentId() {
        return componentId;
    }

    public int getVarbitId() {
        return varbitId;
    }

    public int getDefaultValue() {
        return defaultValue;
    }
}
