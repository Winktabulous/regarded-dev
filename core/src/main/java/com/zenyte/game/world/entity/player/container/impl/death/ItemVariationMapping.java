package com.zenyte.game.world.entity.player.container.impl.death;

import com.google.gson.reflect.TypeToken;
import com.zenyte.cores.ScheduledExternalizable;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Kris | 19/06/2019 12:08
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ItemVariationMapping implements ScheduledExternalizable {
    private static final Logger log = LoggerFactory.getLogger(ItemVariationMapping.class);
    private static final Int2IntMap MAPPINGS = new Int2IntOpenHashMap();

    /**
     * Get base item id for provided variation item id.
     *
     * @param itemId the item id
     * @return the base item id
     */
    public static int map(int itemId) {
        return MAPPINGS.getOrDefault(itemId, itemId);
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public int writeInterval() {
        return -1;
    }

    @Override
    public void read(final @NotNull BufferedReader reader) {
        try {
            final TypeToken<Map<String, Collection<Integer>>> typeToken = new TypeToken<>() {};
            final InputStream geLimitData = new FileInputStream(path());
            final Map<String, Collection<Integer>> itemVariations = getGSON().fromJson(new InputStreamReader(geLimitData), typeToken.getType());
            for (Collection<Integer> value : itemVariations.values()) {
                final Iterator<Integer> iterator = value.iterator();
                final int base = iterator.next();
                while (iterator.hasNext()) {
                    MAPPINGS.put(iterator.next().intValue(), base);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void write() {
    }

    @Override
    public String path() {
        return "data/item_variations.json";
    }

}
