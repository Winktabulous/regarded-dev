package com.zenyte.game.world.entity.player;

import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonDeserializer;
import com.zenyte.plugins.events.InitializationEvent;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mgi.types.config.VarbitDefinitions;

import java.util.Objects;
import java.util.function.IntConsumer;

/**
 * @author Kris | 25. jaan 2018 : 23:59.25
 * @author Jire
 */
@SuppressWarnings("all")
public final class VarManager {
    /**
     * A set of varps that are to be saved on the character file.
     * Set is populated from across the source, generally through enums or other similar collections.
     */
    public static final Int2IntMap persistentVars = new Int2IntOpenHashMap(5000);

    public static final Int2IntMap defaultVarBits = new Int2IntOpenHashMap(5000);

    public static final void appendPersistentVarp(final int varp, final int defaultValue) {
        persistentVars.putIfAbsent(varp, defaultValue);
    }

    public static final void appendPersistentVarp(final int varp) {
        appendPersistentVarp(varp, 0);
    }

    public static final void appendPersistentVarbit(final int varbit, final int defaultValue) {
        VarbitDefinitions.findVarp(varbit)
                .ifPresent((IntConsumer) varp -> {
                    appendPersistentVarp(varp);
                    defaultVarBits.putIfAbsent(varbit, defaultValue);
                });
    }

    public static final void appendPersistentVarbit(final int varbit) {
        appendPersistentVarbit(varbit, 0);
    }

    private static final int[] masks = new int[32];

    static {
        int i = 2;
        for (int i2 = 0; i2 < masks.length; i2++) {
            masks[i2] = i - 1;
            i += i;
        }
    }

    private final transient int[] values;
    private final transient Player player;

    public static final JsonDeserializer<VarManager> deserializer() {
        return (json, typeOfT, context) -> {
            assert json.isJsonObject();
            final com.google.gson.JsonElement elementsMap = Objects.requireNonNull(json.getAsJsonObject().get(
                    "serializedVars"));
            assert elementsMap.isJsonObject();
            final com.zenyte.game.world.entity.player.VarManager manager = new VarManager(null);
            elementsMap.getAsJsonObject().entrySet().forEach(element -> {
                final int key = Integer.parseInt(element.getKey());
                final int value = Integer.parseInt(element.getValue().getAsString());
                manager.serializedVars.put(key, value);
            });
            return manager;
        };
    }

    private final Int2IntMap serializedVars = new Int2IntOpenHashMap();

    @Subscribe
    public static final void onInitialization(final InitializationEvent event) {
        final com.zenyte.game.world.entity.player.Player player = event.getPlayer();
        final com.zenyte.game.world.entity.player.Player saved = event.getSavedPlayer();
        if (saved == null || saved.getVarManager() == null || saved.getVarManager().serializedVars == null) {
            return;
        }
        player.getVarManager().serializedVars.putAll(saved.getVarManager().serializedVars);
    }

    public final void refreshDefaults() {
        for (final Int2IntMap.Entry entry : defaultVarBits.int2IntEntrySet()) {
            final int value = entry.getIntValue();
            if (value == 0) continue;

            final int key = entry.getIntKey();

            final VarbitDefinitions defs = VarbitDefinitions.get(key);
            if (defs == null) continue;

            if (serializedVars.containsKey(defs.getBaseVar())) continue;

            sendBit(key, value);
        }
        for (final Int2IntMap.Entry entry : persistentVars.int2IntEntrySet()) {
            final int value = entry.getIntValue();
            if (value == 0) continue;

            final int key = entry.getIntKey();
            if (serializedVars.containsKey(key)) continue;

            sendVar(key, value);
        }
        for (final Int2IntMap.Entry entry : serializedVars.int2IntEntrySet()) {
            sendVar(entry.getIntKey(), entry.getIntValue());
        }
    }

    public VarManager(final Player player) {
        this.player = player;
        values = new int[20000];//CacheManager.getCache().getArchive(ArchiveType.CONFIGS).findGroupByID(GroupType.VARBIT).getHighestFileId() + 1];
    }

    public void sendVar(final Varp varp, final int value) {
        if (varp instanceof Varbit) {
            setBit(varp.getId(), value);
        } else {
            setVar(varp.getId(), value);
        }
    }

    public void sendVar(final Varp varp, final boolean value) {
        if (varp instanceof Varbit) {
            setBit(varp.getId(), value ? 1 : 0);
        } else {
            setVar(varp.getId(), value ? 1 : 0);
        }
    }

    public void sendVarInstant(final int id, final int value) {
        if (id == -1) {
            return;
        }
        values[id] = value;
        if (persistentVars.containsKey(id)) {
            serializedVars.put(id, value);
        }
        player.getPacketDispatcher().sendConfig(id, value);
    }

    public void sendVar(final int id, final int value) {
        setVar(id, value);
    }

    private void setVar(final int id, final int value) {
        if (id == -1) {
            return;
        }
        values[id] = value;
        if (persistentVars.containsKey(id)) {
            serializedVars.put(id, value);
        }
        player.getPendingVars().add(id);
    }

    public int getValue(final int id) {
        return values[id];
    }

    public void incrementBit(final int id, final int amount) {
        sendBit(id, getBitValue(id) + amount);
    }

    public void decrementBit(final int id) {
        final int result = getBitValue(id) - 1;
        sendBit(id, result < 0 ? 0 : result);
    }

    public void sendBit(final int id, final int value) {
        setBit(id, value);
    }

    public void flipBit(final int id) {
        sendBitInstant(id, getBitValue(id) == 1 ? 0 : 1);
    }

    public void sendBitInstant(final int id, final boolean value) {
        sendBitInstant(id, value ? 1 : 0);
    }

    public void sendBitInstant(final int id, final int value) {
        setBitInstant(id, value);
    }

    public void sendBit(final int id, final boolean value) {
        setBit(id, value ? 1 : 0);
    }

    public int getBitValue(final int id) {
        final VarbitDefinitions defs = VarbitDefinitions.get(id);
        if (defs == null) {
            return 0;
        }
        return values[defs.getBaseVar()] >> defs.getStartBit() & masks[defs.getEndBit() - defs.getStartBit()];
    }

    private void setBit(final int id, int value) {
        if (id == -1) {
            return;
        }
        final VarbitDefinitions defs = VarbitDefinitions.get(id);
        if (defs == null) {
            return;
        }
        int mask = masks[defs.getEndBit() - defs.getStartBit()];
        if (value < 0 || value > mask) {
            value = 0;
        }
        mask <<= defs.getStartBit();
        final int varpValue = (values[defs.getBaseVar()] & (~mask) | value << defs.getStartBit() & mask);
        setVar(defs.getBaseVar(), varpValue);
    }

    private void setBitInstant(final int id, int value) {
        if (id == -1) {
            return;
        }
        final VarbitDefinitions defs = VarbitDefinitions.get(id);
        if (defs == null) {
            return;
        }
        int mask = masks[defs.getEndBit() - defs.getStartBit()];
        if (value < 0 || value > mask) {
            value = 0;
        }
        mask <<= defs.getStartBit();
        final int varpValue = (values[defs.getBaseVar()] & (~mask) | value << defs.getStartBit() & mask);
        sendVarInstant(defs.getBaseVar(), varpValue);
    }

    public Int2IntMap getSerializedVars() {
        return serializedVars;
    }

}
