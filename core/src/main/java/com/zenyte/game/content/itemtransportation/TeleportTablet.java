package com.zenyte.game.content.itemtransportation;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.zenyte.game.item.ItemId.GUTHIXIAN_TEMPLE_TELEPORT;

/**
 * @author Tommeh | 2-4-2018 | 19:43
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum TeleportTablet implements Teleport {
    LUMBRIDGE(new Item(8008), new Location(3222, 3218, 0)),
    GRAND_EXCHANGE(new Item(8007), new Location(3164, 3477, 0)),
    VARROCK(new Item(8007), new Location(3210, 3424, 0)),
    FALADOR(new Item(8009), new Location(2964, 3378, 0)),
    SEERS_VILLAGE(new Item(8010), new Location(2725, 3485, 0)),
    CAMELOT(new Item(8010), new Location(2757, 3477, 0)),
    ARDOUGNE(new Item(8011), new Location(2662, 3305, 0)),
    YANILLE_ALT(new Item(8012), new Location(2584, 3097, 0)),
    WATCHTOWER(new Item(8012), new Location(2549, 3112, 0)),
    KOUREND(new Item(19651), new Location(1645, 3667, 0)),
    TELEPORT_TO_HOUSE(new Item(8013), new Location(-1, -1, -1)),
    RIMMINGTON(new Item(11741), new Location(2956, 3223, 0)),
    TAVERLY(new Item(11742), new Location(2896, 3456, 0)),
    POLLNIVNEACH(new Item(11743), new Location(3356, 2966, 0)),
    RELLEKA(new Item(11744), new Location(2669, 3636, 0)),
    BRIMHAVEN(new Item(11745), new Location(2760, 3178, 0)),
    YANILLE(new Item(11746), new Location(2544, 3092, 0)),
    TROLLHEIM(new Item(11747), new Location(2890, 3676, 0)),
    EDGEVILLE(new Item(CustomItemId.NR_TABLET), new Location(3087, 3489, 0)),
    PADDEWWA(new Item(12781), new Location(3098, 9884, 0)),
    SENNTISTEN(new Item(12782), new Location(3322, 3336, 0)),
    KHARYRLL(new Item(12779), new Location(3492, 3471, 0)),
    LASSAR(new Item(12780), new Location(3006, 3471, 0)),
    DAREEYAK(new Item(12777), new Location(2966, 3695, 0)),
    CARRALLANGAR(new Item(12776), new Location(3156, 3666, 0)),
    ANNAKARL(new Item(12775), new Location(3288, 3886, 0)),
    GHORROCK(new Item(12778), new Location(2977, 3873, 0)),
    ARCEUUS_LIBRARY(new Item(19613), new Location(1633, 3837, 0)),
    DRAYNOR_MANOR(new Item(19615), new Location(3111, 3326, 0)),
    BATTLEFRONT(new Item(22949), new Location(1386, 3721, 0)),
    MIND_ALTAR(new Item(19617), new Location(2976, 3519, 0)),
    SALVE_GRAVEYARD(new Item(19619), new Location(3439, 3487, 0)),
    FENKENSTRAINS_CASTLE(new Item(19621), new Location(3549, 3528, 0)),
    WEST_ARDOUGNE(new Item(19623), new Location(2524, 3306, 0)),
    HARMONY_ISLAND(new Item(19625), new Location(3801, 2857, 0)),
    CEMETARY(new Item(19627), new Location(2976, 3750, 0)),
    BARROWS(new Item(19629), new Location(3565, 3306, 0)),
    APE_ATOLL(new Item(19631), new Location(2778, 2786, 0)),
    VOLCANIC_MINE(new Item(21541), new Location(3814, 3811, 0));
    public static final Int2ObjectOpenHashMap<TeleportTablet> tabs = new Int2ObjectOpenHashMap<>();

    static {
        for (final TeleportTablet tab : values()) {
            for (final Item item : tab.items) {
                tabs.put(item.getId(), tab);
            }
        }
    }

    private final Location location;
    private final Item[] items;

    TeleportTablet(final Item item, final Location location) {
        items = new Item[] {item};
        this.location = location;
    }

    @NotNull
    public static TeleportTablet getTeleportTab(@NotNull final Item item) {
        return Objects.requireNonNull(tabs.get(item.getId()));
    }

    @Override
    public TeleportType getType() {
        return this.equals(VOLCANIC_MINE) ? TeleportType.VOLCANIC_MINE_TELEPORT : this.equals(EDGEVILLE) ? TeleportType.ZENYTE_TABLET_TELEPORT : TeleportType.TABLET_TELEPORT;
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
