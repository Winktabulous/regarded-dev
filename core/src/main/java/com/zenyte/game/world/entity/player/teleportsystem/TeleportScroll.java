package com.zenyte.game.world.entity.player.teleportsystem;

import com.zenyte.utils.StaticInitializer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Kris | 28/03/2019 13:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@StaticInitializer
public enum TeleportScroll {

    BARROWS(12100, "Barrows teleport scroll", PortalTeleport.BARROWS),
    GODWARS(12101, "Godwars teleport scroll", PortalTeleport.GODWARS),
    ZULRAH(12102, "Zulrah teleport scroll", PortalTeleport.ZULRAH),
    KRAKEN(12103, "Kraken teleport scroll", PortalTeleport.KRAKEN),
    CERBERUS(12104, "Cerberus teleport scroll", PortalTeleport.CERBERUS),
    DAGANNOTH_KINGS(12105, "Dagannoth kings teleport scroll", PortalTeleport.DAGANNOTH_KINGS);

    private final int id;
    private final String name;
    private final PortalTeleport teleport;
    static final TeleportScroll[] values = values();
    public static final Int2ObjectMap<TeleportScroll> map = new Int2ObjectOpenHashMap<>();

    static {
        for (final TeleportScroll value : values) {
            map.put(value.id, value);
        }
    }

    TeleportScroll(final int id, final String name, final PortalTeleport teleport) {
        this.id = id;
        this.name = name;
        this.teleport = teleport;
        final ItemDefinitions defs = ItemDefinitions.get(id);
        if (defs != null) {
            defs.setExamine("Scrawled words used to unlock the knowledge of the ancients.");
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PortalTeleport getTeleport() {
        return teleport;
    }
}
