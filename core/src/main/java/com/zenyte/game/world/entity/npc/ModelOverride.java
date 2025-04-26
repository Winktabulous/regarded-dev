package com.zenyte.game.world.entity.npc;

import com.zenyte.game.world.entity.masks.UpdateFlag;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
public class ModelOverride {

    private final NPC npc;

    ModelOverride(NPC npc) {
        this.npc = npc;
    }

    Int2IntOpenHashMap overrides = new Int2IntOpenHashMap();
    IntArrayList modelsCached = new IntArrayList();
    IntArrayList additions = new IntArrayList();
    public ModelOverride models(@NotNull List<Pair<Integer, Integer>> overrides) {
        for(Pair<Integer, Integer> override: overrides) {
            this.overrides.put(override.getFirst().intValue(), override.getSecond().intValue());
        }
        npc.getUpdateFlags().flag(UpdateFlag.MODEL_OVERRIDE);
        return this;
    }

    public void add(Integer... models) {
        for(int model: models) {
            additions.add(model);
        }
    }

    public void reset() {
        overrides.clear();
        additions.clear();
        npc.getUpdateFlags().flag(UpdateFlag.MODEL_OVERRIDE);
    }

    public int[] override(int[] models) {
        modelsCached.clear();
        for(int i: models) {
            if(overrides.containsKey(i))
                i = overrides.get(i);
            modelsCached.add(i);
        }
        for(int i: additions) {
            modelsCached.add(i);
        }
        return modelsCached.toArray(new int[]{});
    }

    public boolean empty() {
        return additions.isEmpty() && overrides.isEmpty();
    }
}