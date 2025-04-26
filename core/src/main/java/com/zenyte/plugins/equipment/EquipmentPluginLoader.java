package com.zenyte.plugins.equipment;

import com.zenyte.game.util.Utils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 25. jaan 2018 : 4:36.14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum EquipmentPluginLoader {
    ;
    

    private static final Logger log = LoggerFactory.getLogger(EquipmentPluginLoader.class);
    private static final Int2ObjectMap<EquipmentPlugin> plugins = new Int2ObjectOpenHashMap<>();
    private static final EquipmentPlugin defaultPlugin;

    static {
        defaultPlugin = new EquipmentPlugin() {
            @Override
            public void handle() {
            }

            @Override
            public int[] getItems() {
                return null;
            }
        };
        defaultPlugin.setDefaultHandlers();
    }

    public static EquipmentPlugin getPlugin(final int key) {
        return Utils.getOrDefault(plugins.get(key), defaultPlugin);
    }

    public static void add(final Class<?> c) {
        try {
            final EquipmentPlugin action = (EquipmentPlugin) c.getDeclaredConstructor().newInstance();
            action.handle();
            action.setDefaultHandlers();
            for (final int item : action.getItems()) {
                plugins.put(item, action);
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }

}
