package com.zenyte.game.world.entity.npc.drop.matrix;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Kris | 18/11/2018 20:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum DropProcessorLoader {
    ;

    private static final Logger log = LoggerFactory.getLogger(DropProcessorLoader.class);
    private static final Int2ObjectMap<List<DropProcessor>> mappedByNPC = new Int2ObjectOpenHashMap<>();
    private static final Int2ObjectMap<List<DropProcessor>> mappedByItem = new Int2ObjectOpenHashMap<>();

    public static List<DropProcessor> get(final int npcID) {
        return mappedByNPC.get(npcID);
    }

    public static Int2ObjectMap<List<DropProcessor>> getProcessors() {
        return mappedByNPC;
    }

    public static boolean contains(final int itemId) {
        return mappedByItem.containsKey(itemId);
    }

    public static List<DropProcessor> getByItem(final int itemId) {
        return mappedByItem.get(itemId);
    }

    public static void add(final Class<?> c) {
        try {
            final DropProcessor dropProcessor = (DropProcessor) c.getDeclaredConstructor().newInstance();
            dropProcessor.attach();

            for (final int npcID : dropProcessor.getAllIds()) {
                List<DropProcessor> list = mappedByNPC.get(npcID);
                if (list == null) {
                    mappedByNPC.put(npcID, list = new ObjectArrayList<>());
                }
                list.add(dropProcessor);
            }
            for (final DropProcessor.DisplayedDrop drop : dropProcessor.getBasicDrops()) {
                final int itemID = drop.getId();

                List<DropProcessor> list = mappedByItem.get(itemID);
                if (list == null) {
                    mappedByItem.put(itemID, list = new ObjectArrayList<>());
                }
                list.add(dropProcessor);
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }

}
