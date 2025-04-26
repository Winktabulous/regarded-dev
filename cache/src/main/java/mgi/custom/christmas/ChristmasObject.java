package mgi.custom.christmas;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mgi.types.config.ObjectDefinitions;

/**
 * @author Kris | 24/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum ChristmasObject {
    CAVE_ENTRANCE(8930, 40027), HOLE(19640, 40028), HOLLOW_LOG(19425, 40029), CHEST(32758, 40030), TUNNEL(19424, 40031), SNOW_DRIFT(19435, 40032), ALT_HOLLOW_LOG(19422, 40033);
    private final int originalObject;
    private final int repackedObject;
    public static Int2IntMap redirectedIds = new Int2IntOpenHashMap();

    ObjectDefinitions.ObjectDefinitionsBuilder builder() {
        return ObjectDefinitions.getOrThrow(originalObject).toBuilder().id(repackedObject);
    }

    static {
        for (final ChristmasObject value : values()) {
            redirectedIds.put(value.originalObject, value.repackedObject);
        }
    }

    ChristmasObject(int originalObject, int repackedObject) {
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
