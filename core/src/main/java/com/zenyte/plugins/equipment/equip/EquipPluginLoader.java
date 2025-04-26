package com.zenyte.plugins.equipment.equip;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 25. jaan 2018 : 4:36.14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum EquipPluginLoader {
    ;

    private static final Logger log = LoggerFactory.getLogger(EquipPluginLoader.class);

    public static final Int2ObjectMap<EquipPlugin> plugins = new Int2ObjectOpenHashMap<>();

    public static void add(final Class<?> c) {
        try {
            final EquipPlugin action = (EquipPlugin) c.getDeclaredConstructor().newInstance();
            for (final int item : action.getItems()) {
                plugins.put(item, action);
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }

}
