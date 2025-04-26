package com.zenyte.game.content.skills.prayer.ectofuntus;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 * @author Kris | 26/06/2019 12:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BonemealItem extends ItemPlugin {
    @Override
    public void handle() {
        bind("Empty", (player, item, container, slotId) -> {
            player.getInventory().set(slotId, new Item(1931));
            player.getInventory().refreshAll();
        });
    }

    @Override
    public int[] getItems() {
        final IntOpenHashSet set = new IntOpenHashSet();
        for (final BoneGrinding.Bonemeal bonemeal : BoneGrinding.Bonemeal.values) {
            set.add(bonemeal.getBonemeal());
        }
        return set.toIntArray();
    }
}
