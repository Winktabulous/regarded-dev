package com.zenyte.game.content.skills.construction;

import com.zenyte.game.content.skills.magic.spells.teleports.SpellbookTeleport;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 25. veebr 2018 : 22:59.36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum TeleportPortal {

	VARROCK_PORTAL(SpellbookTeleport.VARROCK_TELEPORT, 13615, 13622, 13629),
	LUMBRIDGE_PORTAL(SpellbookTeleport.LUMBRIDGE_TELEPORT, 13616, 13623, 13630),
	FALADOR_PORTAL(SpellbookTeleport.FALADOR_TELEPORT, 13617, 13624, 13631),
	CAMELOT_PORTAL(SpellbookTeleport.CAMELOT_TELEPORT, 13618, 13625, 13632),
	ARDOUGNE_PORTAL(SpellbookTeleport.ARDOUGNE_TELEPORT, 13619, 13626, 13633),
    YANILLE_PORTAL(SpellbookTeleport.WATCHTOWER_TELEPORT, 13620, 13627, 13634),
    KHARYRLL_PORTAL(SpellbookTeleport.KHARYRLL_TELEPORT, 29338, 29346, 299354),
    LUNAR_ISLE_PORTAL(SpellbookTeleport.LUNAR_HOME_TELEPORT, 29339, 29347, 29355),
    SENNTISTEN_PORTAL(SpellbookTeleport.SENNTISTEN_TELEPORT, 29340, 29348, 29356),
    ANNAKARL_PORTAL(SpellbookTeleport.ANNAKARL_TELEPORT, 29341, 29349, 29357),
    WATERBIRTH_ISLAND_PORTAL(SpellbookTeleport.WATERBIRTH_TELEPORT, 29342, 29350, 29358),
    FISHING_GUILD_PORTAL(SpellbookTeleport.FISHING_GUILD_TELEPORT, 29343, 29351, 29359),
    MARIM_PORTAL(SpellbookTeleport.APE_ATOLL_TELEPORT_REG, 29344, 29352, 29360),
    KOUREND_PORTAL(SpellbookTeleport.KOUREND_CASTLE_TELEPORT, 29345, 29353, 29361);

    public static final TeleportPortal[] VALUES = values();
    public static final Map<Integer, TeleportPortal> MAP = new HashMap<Integer, TeleportPortal>(VALUES.length * 3);

    static {
        for (final TeleportPortal val : VALUES) {
            for (final int i : val.portalIds) {
                MAP.put(i, val);
            }
        }
    }

    private final SpellbookTeleport teleport;
    private final int[] portalIds;

    TeleportPortal(final SpellbookTeleport teleport, final int... portalIds) {
        this.teleport = teleport;
        this.portalIds = portalIds;
    }

    public SpellbookTeleport getTeleport() {
        return teleport;
    }

    public int[] getPortalIds() {
        return portalIds;
    }
}
