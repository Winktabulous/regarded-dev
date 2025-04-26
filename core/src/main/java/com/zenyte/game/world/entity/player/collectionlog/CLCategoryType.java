package com.zenyte.game.world.entity.player.collectionlog;

import com.zenyte.utils.Ordinal;
import mgi.utilities.StringFormatUtil;

/**
 * @author Kris | 13/03/2019 14:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */

@Ordinal
public enum CLCategoryType {

    BOSS(471, 2103), RAIDS(472), CLUES(473), MINIGAMES(474), OTHER(475);

    public static final CLCategoryType[] values = values();
    private final int struct;
    private final int enumPtr;
    private final String toString = StringFormatUtil.formatString(name());
    private final String category = this + " category";
    
    CLCategoryType(int struct) {
        this.struct = struct;
        this.enumPtr = -1;
    }
    CLCategoryType(int struct, int enumPtr) {
        this.struct = struct;
        this.enumPtr = enumPtr;
    }

    public int struct() {
        return struct;
    }

    public int enumPtr() {
        return enumPtr;
    }
    
    public String toString() {
        return toString;
    }
    
    public String category() {
        return category;
    }

}
