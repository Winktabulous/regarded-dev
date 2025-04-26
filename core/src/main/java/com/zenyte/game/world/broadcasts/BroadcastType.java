package com.zenyte.game.world.broadcasts;

import com.zenyte.game.world.entity.player.GameSetting;

import java.util.Optional;

/**
 * @author Tommeh | 5-2-2019 | 00:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum BroadcastType {

    HELPFUL_TIP("7881fd", 13, Optional.of(GameSetting.HELPFUL_TIPS)),
    RARE_DROP("e59400", 13, Optional.of(GameSetting.RARE_DROP_BROADCASTS)),
    LVL_99("e59400", 13, Optional.of(GameSetting.LEVEL_99_BROADCASTS)),
    MAXED("ff0000", 13, Optional.of(GameSetting.MAXED_PLAYER_BROADCASTS)),
    XP_200M("ff0000", 13, Optional.of(GameSetting.MAX_SKILL_XP_BROADCASTS)),
    PET("ff0000", 13, Optional.of(GameSetting.PET_BROADCASTS)),
    GAMBLE_FIRECAPE("ff0000", 13, Optional.empty()),
    HCIM_DEATH("ff0000", 8, Optional.of(GameSetting.HARDCORE_IRONMAN_DEATH_BROADCASTS)),
    GROUP_HCIM_DEATH("ff0000", 8, Optional.of(GameSetting.HARDCORE_IRONMAN_DEATH_BROADCASTS)),
    MYSTERY_BOX_RARE_ITEM("ff0000", 13, Optional.of(GameSetting.MYSTERY_BOX_BROADCASTS)),
    INFERNO_COMPLETION("ff0000", 13, Optional.empty()),
    TREASURE_TRAILS("e59400", 13, Optional.of(GameSetting.TREASURE_TRAILS_BROADCASTS)),
    WILDERNESS_EVENT("B22222", 68, Optional.empty()),
    XAMPHUR("e59400", 13, Optional.empty()),
    SUPER_RARE_DROP("B22222", 51, Optional.empty()),

    WILDERNESS_VAULT("e59400", 13, Optional.empty()),
    LOTTERY("e59400", 50, Optional.empty()),
    WELL_OF_GOODWILL("e59400", 49, Optional.empty()),
    NEW_PLAYER("e59400", 53, Optional.of(GameSetting.NEW_PLAYERS)),
    SUPPORT_LOGIN("00b8ff", 53, Optional.empty()),
    MOD_LOGIN("c6cad1", 53, Optional.empty()),
    ADMIN_LOGIN("e4df28", 53, Optional.empty()),
    DEV_LOGIN("002366", 53, Optional.empty()),
    MALEDICTUS("e59400", 68, Optional.empty()),
    COLOSSAL_CHICKEN("e59400", 13, Optional.empty()),
    PVM_ARENA("e59400", 13, Optional.empty()),
    BOUNTY_HUNTER("e59400", 57, Optional.empty()),
    ;

    private final String color;
    private final int icon;
    private final Optional<GameSetting> setting;

    public static final BroadcastType[] VALUES = values();

    BroadcastType(String color, int icon, Optional<GameSetting> setting) {
        this.color = color;
        this.icon = icon;
        this.setting = setting;
    }

    public String getColor() {
        return color;
    }

    public int getIcon() {
        return icon;
    }

    public Optional<GameSetting> getSetting() {
        return setting;
    }

}
