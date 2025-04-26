package com.zenyte.game.world.entity.player.privilege;

/**
 * !!! DO NOT RENAME ENUMS IN HERE OR IT WILL BREAK {@link com.near_reality.game.content.middleman.MiddleManStaffOption.GsonAdapter}.
 */
public enum Crown {
    NONE(-1, CrownType.NONE),
    STANDARD_IRON_MAN(2, CrownType.GAME_MODE),
    ULTIMATE_IRON_MAN(3, CrownType.GAME_MODE),
    HARDCORE_IRON_MAN(10, CrownType.GAME_MODE),
    PREMIUM(14, CrownType.MEMBER),
    EXPANSION(15, CrownType.MEMBER),
    EXTREME(16, CrownType.MEMBER),
    RESPECTED(17, CrownType.MEMBER),
    LEGENDARY(18, CrownType.MEMBER),
    MYTHICAL(19, CrownType.MEMBER),
    UBER(20, CrownType.MEMBER),

    YOUTUBER(7, CrownType.RANK),
    FORUM_MODERATOR(6, CrownType.RANK),
    SUPPORT(4, CrownType.RANK),
    MODERATOR(0, CrownType.RANK),
    SENIOR_MODERATOR(0, CrownType.RANK),
    ADMINISTRATOR(1, CrownType.RANK),
    DEVELOPER(5, CrownType.RANK),
    TRUE_DEVELOPER(69, CrownType.RANK),

    GROUP_IRON_MAN(41, CrownType.GAME_MODE),
    HARDCORE_GROUP_IRON_MAN(42, CrownType.GAME_MODE),
    REALIST_REGULAR(43, CrownType.GAME_MODE),
    REALIST_STANDARD_IRON_MAN(44, CrownType.GAME_MODE),
    REALIST_HARDCORE_IRON_MAN(45, CrownType.GAME_MODE),
    REALIST_ULTIMATE_IRON_MAN(46, CrownType.GAME_MODE),

    AMASCUT(52, CrownType.MEMBER),
    ;
    private final int id;
    private final CrownType type;
    private final String tag;

    Crown(int id, CrownType type) {
        this.id = id;
        this.type = type;
        this.tag = "<img=" + id + ">";
    }

    public int getId() {
        return id;
    }

    public CrownType getType() {
        return type;
    }

    public String getCrownTag() {
        return tag;
    }
}
