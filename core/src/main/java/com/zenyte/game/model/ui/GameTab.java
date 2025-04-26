package com.zenyte.game.model.ui;

/**
 * @author Tommeh | 30 jan. 2018 : 21:08:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum GameTab {

	COMBAT_TAB(0),
	SKILLS_TAB(1),
	QUEST_TAB(2),
	INVENTORY_TAB(3),
	EQUIPMENT_TAB(4),
    PRAYER_TAB(5),
    SPELLBOOK_TAB(6),
    CLAN_CHAT_TAB(7),
    FRIENDS_TAB(8),
    IGNORES_TAB(9),
    LOGOUT_TAB(10),
    SETTINGS_TAB(11),
    EMOTES_TAB(12),
    MUSIC_TAB(13);

    private final int id;
    private final int[] tabIds;

    GameTab(int id) {
        this.id = id;
        this.tabIds = new int[3];
        this.tabIds[0] = 66 + ordinal();
        this.tabIds[1] = 68 + ordinal();
        this.tabIds[2] = 66 + ordinal();
    }

    public int getId() {
        return id;
    }

    public int[] getTabIds() {
        return tabIds;
    }
}