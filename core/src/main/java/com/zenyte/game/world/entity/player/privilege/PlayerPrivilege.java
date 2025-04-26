package com.zenyte.game.world.entity.player.privilege;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * IMPORTANT: some staff checks depend on the ordinal of the privilege,
 * so do NOT add new privilege types below staff types.
 */
public enum PlayerPrivilege implements IPrivilege {

    PLAYER("Player", 0, Crown.NONE, "000000", false),
    MEMBER("Member", 2, Crown.NONE, "000000", false),

    YOUTUBER("YouTuber", 2, Crown.YOUTUBER, "ff0000", false,
            PlayerPrivilege.PLAYER,
            PlayerPrivilege.MEMBER
    ),

    FORUM_MODERATOR("QA Team", 2, Crown.FORUM_MODERATOR, "cc6eee", true,
            PlayerPrivilege.PLAYER,
            PlayerPrivilege.MEMBER
    ),

    SUPPORT("Support", 4, Crown.SUPPORT, "00b8ff", true,
            PlayerPrivilege.FORUM_MODERATOR,
            PlayerPrivilege.PLAYER,
            PlayerPrivilege.MEMBER
    ),

    MODERATOR("Moderator", 5, Crown.MODERATOR, "c6cad1", true,
            PlayerPrivilege.FORUM_MODERATOR,
            PlayerPrivilege.SUPPORT,
            PlayerPrivilege.PLAYER,
            PlayerPrivilege.MEMBER
    ),

    SENIOR_MODERATOR("Senior Moderator", 6, Crown.SENIOR_MODERATOR, "5bf45b", true,
            PlayerPrivilege.MODERATOR,
            PlayerPrivilege.FORUM_MODERATOR,
            PlayerPrivilege.SUPPORT,
            PlayerPrivilege.PLAYER,
            PlayerPrivilege.MEMBER
    ),

    ADMINISTRATOR("Administrator", 7, Crown.ADMINISTRATOR, "e4df28", true,
            PlayerPrivilege.SENIOR_MODERATOR,
            PlayerPrivilege.MODERATOR,
            PlayerPrivilege.FORUM_MODERATOR,
            PlayerPrivilege.SUPPORT,
            PlayerPrivilege.PLAYER,
            PlayerPrivilege.MEMBER
    ),

    DEVELOPER("Manager", 8, Crown.DEVELOPER, "e4df28", true,
            PlayerPrivilege.ADMINISTRATOR,
            PlayerPrivilege.SENIOR_MODERATOR,
            PlayerPrivilege.MODERATOR,
            PlayerPrivilege.FORUM_MODERATOR,
            PlayerPrivilege.SUPPORT,
            PlayerPrivilege.PLAYER,
            PlayerPrivilege.MEMBER
    ),
    HIDDEN_ADMINISTRATOR("Hidden Administrator", 0, Crown.NONE, "000000", false,
            PlayerPrivilege.DEVELOPER,
            PlayerPrivilege.ADMINISTRATOR,
            PlayerPrivilege.SENIOR_MODERATOR,
            PlayerPrivilege.MODERATOR,
            PlayerPrivilege.FORUM_MODERATOR,
            PlayerPrivilege.SUPPORT,
            PlayerPrivilege.PLAYER,
            PlayerPrivilege.MEMBER
    ),
    TRUE_DEVELOPER("Sr. Developer", 69, Crown.TRUE_DEVELOPER, "002366", true,
            PlayerPrivilege.DEVELOPER,
            PlayerPrivilege.HIDDEN_ADMINISTRATOR,
            PlayerPrivilege.ADMINISTRATOR,
            PlayerPrivilege.SENIOR_MODERATOR,
            PlayerPrivilege.MODERATOR,
            PlayerPrivilege.FORUM_MODERATOR,
            PlayerPrivilege.SUPPORT,
            PlayerPrivilege.PLAYER,
            PlayerPrivilege.MEMBER
    )
    ;

    private final String prettyName;
    private final int loginCode;
    private final Crown crown;
    private final String yellColor;
    private final boolean pMod;
    private final ObjectArrayList<PlayerPrivilege> inheritance;


    PlayerPrivilege(String prettyName, int loginCode, Crown crown, String yellColor, boolean pMod, PlayerPrivilege... inherits) {
        this.prettyName = prettyName;
        this.loginCode = loginCode;
        this.crown = crown;
        this.yellColor = yellColor;
        this.pMod = pMod;
        this.inheritance = new ObjectArrayList<>(inherits);

        /* all rights natively inherit themselves */
        this.inheritance.add(this);

        /* all rights inherited should be cascaded into the inheritance, up to 3 layers */
        for(PlayerPrivilege impl: this.inheritance) {
            for(PlayerPrivilege firstChild: impl.inheritance) {
                if(!this.inheritance.contains(firstChild))
                    inheritance.add(firstChild);

                for(PlayerPrivilege secondChild: firstChild.inheritance) {
                    if(!this.inheritance.contains(secondChild))
                        inheritance.add(secondChild);
                    for(PlayerPrivilege thirdChild: secondChild.inheritance) {
                        if(!this.inheritance.contains(thirdChild))
                            inheritance.add(thirdChild);
                    }
                }
            }
        }
    }

    public int getLoginCode() {
        return loginCode;
    }

    public String getYellColor() {
        return yellColor;
    }

    public boolean isPMod() {
        return pMod;
    }

    public String getPrettyName() {
        return prettyName;
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public Crown crown() {
        return crown;
    }


    /**
     * @deprecated use {@link #inherits} for all future privilege checking
     */
    @Deprecated
    public boolean eligibleTo(PlayerPrivilege p) {
        return this.inherits(p);
    }

    public boolean inherits(PlayerPrivilege playerPrivilege) {
        return inheritance.contains(playerPrivilege);
    }

    public boolean is(PlayerPrivilege playerPrivilege) {
        return this == playerPrivilege;
    }
}
