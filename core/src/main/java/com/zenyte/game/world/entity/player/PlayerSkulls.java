package com.zenyte.game.world.entity.player;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.content.lootkeys.LootkeyConstants;
import com.zenyte.game.content.skills.magic.spells.PlayerSpell;
import com.zenyte.game.world.entity.player.variables.TickVariable;

public enum PlayerSkulls {

    NONE(-1),
    DEFAULT(0, 3),
    RED_DEFAULT(1),
    HIGH_RISK(2),
    LOOT_KEY_1(8, 15),
    LOOT_KEY_2(9, 16),
    LOOT_KEY_3(10, 17),
    LOOT_KEY_4(11, 18),
    LOOT_KEY_5(12, 19),
    BLACK_SKULL(20),

    REVIVE_1(21),
    REVIVE_2(22),
    REVIVE_3(23),
    REVIVE_4(24),
    REVIVE_5(25),
    REVIVE_6(26),
    REVIVE_7(27),
    REVIVE_8(28),
    REVIVE_9(29),
    REVIVE_10(30)

    ;

    private final int skullStatusId;
    private final int forinthrySurgeSkullStatusId;

    PlayerSkulls(int skullStatusId, int forinthrySurgeSkullStatusId) {
        this.skullStatusId = skullStatusId;
        this.forinthrySurgeSkullStatusId = forinthrySurgeSkullStatusId;
    }

    PlayerSkulls(int skullStatusId) {
        this.skullStatusId = skullStatusId;
        this.forinthrySurgeSkullStatusId = skullStatusId;
    }

    public static int getSkull(Player player) {

        final int revivalTimer = PlayerAttributesKt.getPvmArenaRevivalCount(player);
        if (revivalTimer > 0) {
            PlayerSkulls skull = PlayerSkulls.valueOf("REVIVE_" + revivalTimer);
            return skull.skullStatusId;
        }

        if (!player.getVariables().isSkulled())
            return NONE.skullStatusId;

        if (PlayerAttributesKt.getBlackSkulled(player))
            return BLACK_SKULL.skullStatusId;

        PlayerSkulls skull = getLootKeySkull(player);

        int time = player.getVariables().getTime(TickVariable.FORINTHRY_SURGE);
        if (time > 0) {
            // If the player has Forinthry surge active, then use that skull instead
            return skull.forinthrySurgeSkullStatusId;
        } else {
            return skull.skullStatusId;
        }
    }

    private static PlayerSkulls getLootKeySkull(Player player) {
        int keyCount = player.getInventory().getAmountOf(LootkeyConstants.LOOT_KEY_ITEM_ID);
        switch (keyCount) {
            case 0:
                return DEFAULT;
            case 1:
                return LOOT_KEY_1;
            case 2:
                return LOOT_KEY_2;
            case 3:
                return LOOT_KEY_3;
            case 4:
                return LOOT_KEY_4;
            default:
                return LOOT_KEY_5;
        }
    }

}
