package mgi.types.config.identitykit;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Tommeh | 13-2-2019 | 17:08
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum HairStyle {

    BALD_MALE(0, 0, true),
    DREADLOCKS_MALE(1, 1, true),
    LONG_MALE(2, 2, true),
    MEDIUM_MALE(3, 3, true),
    TONSURE(4, 4, true),
    SHORT_MALE(5, 5, true),
    CROPPED_MALE(6, 6, true),
    WILD_SPIKES_MALE(7, 7, true),
    SPIKES(8, 8, true),
    MOHAWK(9, 9, true),
    WIND_BRAIDS_MALE(10, 129, true),
    QUIFF(11, 130, true),
    SAMURAI(12, 131, true),
    PRINCELY(13, 132, true),
    CURTAINS_MALE(14, 133, true),
    LONG_CURTAINS(15, 134, true),
    THE_SUPREME_LEADER_MALE(16, 142, true),
    TOUSLED(17, 144, true),
    SIDE_WEDGE(18, 145, true),
    FRONT_WEDGE(19, 146, true),
    FRONT_SPIKES(20, 147, true),
    FROHAWK(21, 148, true),
    REAR_SHIRT(22, 149, true),
    QUEVE(23, 150, true),
    BALD_FEMALE(0, 45, false),
    BUN(1, 46, false),
    DREADLOCKS_FEMALE(2, 47, false),
    LONG_FEMALE(3, 48, false),
    MEDIUM_FEMALE(4, 49, false),
    PIGTAILS(5, 50, false),
    SHORT_FEMALE(6, 51, false),
    CROPPED_FEMALE(7, 52, false),
    WILD_SPIKES_FEMALE(8, 53, false),
    SPIKY(9, 54, false),
    EARMUFFS(10, 55, false),
    SIDE_PONY(11, 118, false),
    CURLS(12, 119, false),
    WIND_BRAIDS_FEMALE(13, 120, false),
    PONYTAIL(14, 121, false),
    BRAIDS(15, 122, false),
    BUNCHES(16, 123, false),
    BOB(17, 124, false),
    LAYERED(18, 125, false),
    STRAIGHT(19, 126, false),
    STRAIGHT_BRAIDS(20, 127, false),
    CURTAINS_FEMALE(21, 128, false),
    THE_SUPREME_LEADER_FEMALE(22, 141, false),
    TWO_BACK(23, 143, false);
    private final int slotId;
    private final int id;
    private final boolean male;
    private static final HairStyle[] values = values();
    private static final Int2ObjectMap<HairStyle> styles = new Int2ObjectOpenHashMap<>(values.length);

    static {
        for (final HairStyle style : values) {
            styles.put(style.slotId & 63 | ((style.male ? 1 : 0) & 1) << 6, style);
        }
    }

    public static int getStyle(final int slotId, final boolean male) {
        final HairStyle style = styles.get(slotId & 63 | ((male ? 1 : 0) & 1) << 6);
        return style == null ? -1 : style.id;
    }

    HairStyle(int slotId, int id, boolean male) {
        this.slotId = slotId;
        this.id = id;
        this.male = male;
    }

    public int getSlotId() {
        return slotId;
    }

    public int getId() {
        return id;
    }

    public boolean isMale() {
        return male;
    }

}
