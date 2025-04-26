package mgi.custom.halloween;

import com.zenyte.ContentConstants;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mgi.types.config.ObjectDefinitions;

public enum HalloweenObject {

    DEAD_EXPLORER(20788, 40001),
    TRAPDOOR(32643, 40003),
    BOAT(17953, 40004),
    DEAD_TREE(4531, 40005),
    TWISTED_BUSH(34712, 40006),
    DEAD_WILLOW(8500, 40007),
    PUMP_AND_DRAIN(13559, 40008),
    DEAD_YEW(8533, 40009),
    CAGE(13322, 14977),
    STRANGE_FLOOR(16510, 40011),
    FIRE_WALL(2908, 40012),
    STEPPING_STONES(23647, 40013),
    DRAYNOR_MANOR_LEFT_DOOR(134, 40014),
    DRAYNOR_MANOR_RIGHT_DOOR(135, 40015),
    BLOODY_AXE(31862, 40016),
    SKELETON(14674, 40017),
    TORCH(13201, 40018),
    CELL_DOOR(9565, 40019),
    PIANO(30134, 40020),
    RANGE(27516, 40021),
    WEB(733, 40022),
    BARRIER(31808, 40023),
    GATE(14979, 40023),
    PILE_OF_RUBBLE(12747, 40024),
    CHEST(2361, 40025),
    OPENED_CHEST(2360, 40026);

    private final int originalObject;
    private final int repackedObject;
    static Int2IntMap redirectedIds = new Int2IntOpenHashMap();

    ObjectDefinitions.ObjectDefinitionsBuilder builder() {
        return ObjectDefinitions.getOrThrow(originalObject).toBuilder().id(repackedObject);
    }

    static {
        for (final HalloweenObject value : values()) {
            if (!ContentConstants.HALLOWEEN) {
                if (value == DRAYNOR_MANOR_LEFT_DOOR || value == DRAYNOR_MANOR_RIGHT_DOOR) {
                    continue;
                }
            }
            redirectedIds.put(value.originalObject, value.repackedObject);
        }
    }

    HalloweenObject(int originalObject, int repackedObject) {
        this.originalObject = originalObject;
        this.repackedObject = repackedObject;
    }

    public int getOriginalObject() {
        return originalObject;
    }

    public int getRepackedObject() {
        return repackedObject;
    }
}
