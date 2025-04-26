package com.zenyte.plugins.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.action.misc.MithrilSeedPlanting;

/**
 * @author Tommeh | 28-4-2019 | 14:47
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class MithrilSeeds extends ItemPlugin {

    @Override
    public void handle() {
        bind("Plant", (player, item, slotId) -> {
            if (!player.isStunned()) {
                final MithrilSeedPlanting.Flowers flowers = MithrilSeedPlanting.Flowers.random();
                player.getActionManager().setAction(new MithrilSeedPlanting(flowers));
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.MITHRIL_SEEDS};
    }
}
