package com.zenyte.game.content.achievementdiary;

/**
 * @author Tommeh | 6-11-2018 | 22:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum DiaryArea {

    ARDOUGNE("Ardougne", 1),
    LUMBRIDGE_AND_DRAYNOR("Lumbridge & Draynor", 6),
    VARROCK("Varrock", 8),
    FALADOR("Falador", 2),
    MORYTANIA("Morytania", 7),
    KARAMJA("Karamja", 0),
    DESERT("Desert", 5),
    WESTERN_PROVINCES("Western Provinces", 10),
    KANDARIN("Kandarin", 4),
    FREMENNIK("Fremennik", 3),
    WILDERNESS("Wilderness", 9),
    KEBOS("Kourend & Kebos Lowlands", 11);

    private final String areaName;
    private final int index;

    public static final DiaryArea[] VALUES = values();

    DiaryArea(String areaName, int index) {
        this.areaName = areaName;
        this.index = index;
    }

    public String getAreaName() {
        return areaName;
    }

    public int getIndex() {
        return index;
    }
}
