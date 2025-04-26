package com.zenyte.game.model.ui;

import mgi.types.config.enums.EnumDefinitions;

/**
 * @author Tommeh | 1 apr. 2018 | 20:00:07
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public enum PaneType {

    FIXED(548, 1129),
    RESIZABLE(161, 1130),
    SIDE_PANELS(164, 1131),
    FULL_SCREEN(165, 1132),
    ORB_OF_OCULUS(16, -1),
    GAME_SCREEN(80, -1),
    CHATBOX(162, -1),
    LOADING_CHATBOX(293, -1),
    JOURNAL_TAB_HEADER(629, -1),
    ADVANCED_SETTINGS(134, -1),
    CHAT_TAB_HEADER(707, -1),
    IRON_GROUP_SOCIALS_TAB_HEADER(727, -1),
    IRON_GROUP_SETTINGS(730, -1),
    IRON_BANK(724, -1),
    MOBILE(601, 1745),
    TOA_MANAGEMENT(774, -1),
    ;

    private final int id;
    private final int enumId;
    
    public final EnumDefinitions getEnum() {
    	if (enumId == -1) {
    		throw new RuntimeException("No enum exists for the pane: " + this + ".");
    	}
    	return EnumDefinitions.get(enumId);
    }
    
    PaneType(int id, int enumId) {
        this.id = id;
        this.enumId = enumId;
    }
    
    public int getId() {
        return id;
    }
    
    public int getEnumId() {
        return enumId;
    }

}
