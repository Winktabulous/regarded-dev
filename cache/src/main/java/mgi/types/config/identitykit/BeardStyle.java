package mgi.types.config.identitykit;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * @author Tommeh | 13-2-2019 | 16:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum BeardStyle {
    
    CLEAN_SHAVEN(0, 14),
    GOATEE(1, 10),
    LONG(2, 11),
    MEDIUM(3, 12),
    SMALL_MOUSTACHE(4, 13),
    SHORT(5, 15),
    POINTY(6, 16),
    SPLIT(7, 17),
    HANDLEBAR(8, 111),
    MUTTON(9, 112),
    FULL_MOTTON(10, 113),
    BIG_MOUSTACHE(11, 114),
    WAXED_MOUSTACHE(12, 115),
    DALI(13, 116),
    VIZIER(14, 117);

    private final int slotId;
    private final int id;
    private static final BeardStyle[] values = values();
    private static final Int2IntMap styles = new Int2IntOpenHashMap(values.length);

    static {
        for (final BeardStyle style : values) {
            styles.put(style.slotId, style.id);
        }
    }

    public static int getStyle(final int slotId) {
        return styles.get(slotId);
    }

    BeardStyle(int slotId, int id) {
        this.slotId = slotId;
        this.id = id;
    }

    public int getSlotId() {
        return slotId;
    }

    public int getId() {
        return id;
    }

}
