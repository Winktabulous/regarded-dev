package mgi.custom.halloween;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mgi.types.config.npcs.NPCDefinitions;

/**
 * @author Kris | 03/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum HalloweenNPC {
    JONAS_SHORTSWORD(4222, 10022), JONAS_LONGSWORD(4222, 10023), JONAS_UNMASKED(4222, 10024), SHILOP(3501, 10025), CROW(2069, 10026), GHOST(922, 10027), GRIM_REAPER_BASE(4222, 10028), GRIM_REAPER(4222, 10029);
    private final int originalNPC;
    private final int repackedNPC;
    static Int2IntMap redirectedIds = new Int2IntOpenHashMap();

    NPCDefinitions.NPCDefinitionsBuilder builder() {
        return NPCDefinitions.getOrThrow(originalNPC).toBuilder().id(repackedNPC);
    }

    static {
        for (final HalloweenNPC value : values()) {
            redirectedIds.put(value.originalNPC, value.repackedNPC);
        }
    }

    HalloweenNPC(int originalNPC, int repackedNPC) {
        this.originalNPC = originalNPC;
        this.repackedNPC = repackedNPC;
    }

    public int getOriginalNPC() {
        return originalNPC;
    }

    public int getRepackedNPC() {
        return repackedNPC;
    }
}
