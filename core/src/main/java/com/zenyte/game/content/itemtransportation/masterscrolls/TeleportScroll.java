package com.zenyte.game.content.itemtransportation.masterscrolls;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import static com.zenyte.game.item.ItemId.GUTHIXIAN_TEMPLE_TELEPORT;
import static com.zenyte.game.item.ItemId.SPIDER_CAVE_TELEPORT;

/**
 * @author Tommeh | 2-4-2018 | 19:43
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum TeleportScroll implements Teleport {
    DIGSITE(new Item(12403), new Location(3324, 3412, 0)),
    ELF_CAMP(new Item(12410), new Location(2195, 3253, 0)),
    FELLDIP_HILLS(new Item(12404), new Location(2556, 2982, 0)),
    LUMBERYARD(new Item(12642), new Location(3306, 3483, 0)),
    LUNAR_ISLE(new Item(12405), new Location(2108, 3914, 0)),
    MORT_TON(new Item(12406), new Location(3487, 3284, 0)),
    MOS_LE_HARMLESS(new Item(12411), new Location(3677, 2976, 0)),
    NARDAH(new Item(12402), new Location(3423, 2914, 0)),
    PEST_CONTROL(new Item(12407), new Location(2662, 2647, 0)),
    PISCATORIS(new Item(12408), new Location(2342, 3692, 0)),
    TAI_BWO_WANNAI(new Item(12409), new Location(2790, 3065, 0)),
    ZUL_ANDRA(new Item(12938), new Location(2200, 3055, 0)),
    KEY_MASTER(new Item(13249), new Location(1309, 1250, 0)),
    REVENANT_CAVE(new Item(21802), new Location(3127, 3833, 0)),
    WATSON(new Item(23387), new Location(1642, 3576, 0)),
    GUTHIX_TEMPLE(new Item(GUTHIXIAN_TEMPLE_TELEPORT), new Location(4077, 4461, 0)),
    SPIDY_CAVE(new Item(SPIDER_CAVE_TELEPORT), new Location(3661, 9812, 0))
    ;
    public static final Int2ObjectMap<TeleportScroll> scrolls = new Int2ObjectOpenHashMap<>();

    static {
        for (final TeleportScroll tab : values()) {
            for (final Item item : tab.items) {
                scrolls.put(item.getId(), tab);
            }
        }
    }

    private final Location location;
    private final Item[] items;

    TeleportScroll(final Item item, final Location location) {
        items = new Item[] {item};
        this.location = location;
    }

    public static TeleportScroll getTeleportScroll(final Item item) {
        return scrolls.get(item.getId());
    }

    @Override
    public TeleportType getType() {
        return TeleportType.SCROLL_TELEPORT;
    }

    @Override
    public Location getDestination() {
        return location;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public double getExperience() {
        return 0;
    }

    @Override
    public int getRandomizationDistance() {
        return DISTANCE;
    }

    @Override
    public Item[] getRunes() {
        return items;
    }

    @Override
    public int getWildernessLevel() {
        return WILDERNESS_LEVEL;
    }

    @Override
    public boolean isCombatRestricted() {
        return UNRESTRICTED;
    }

    public Location getLocation() {
        return location;
    }

    public Item[] getItems() {
        return items;
    }
}
