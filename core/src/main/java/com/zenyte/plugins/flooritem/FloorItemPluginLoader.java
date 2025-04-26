package com.zenyte.plugins.flooritem;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 27. march 2018 : 21:58.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum FloorItemPluginLoader {
    ;

    private static final Logger log = LoggerFactory.getLogger(FloorItemPluginLoader.class);

    public static final Int2ObjectMap<FloorItemPlugin> plugins = new Int2ObjectOpenHashMap<>();

    public static void add(final Class<?> c) {
        try {
            final FloorItemPlugin action = (FloorItemPlugin) c.getDeclaredConstructor().newInstance();
            for (final int item : action.getItems()) {
                plugins.put(item, action);
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }

}
