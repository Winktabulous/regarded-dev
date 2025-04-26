package com.zenyte.plugins.flooritem;

import com.zenyte.game.content.skills.magic.spells.arceuus.Reanimation;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 24/04/2019 13:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EnsouledHead implements FloorItemPlugin {
    @Override
    public boolean overrideTake() {
        return true;
    }

    @Override
    public void telegrab(@NotNull final Player player, @NotNull final FloorItem item) {
        if (!canTelegrab(player, item)) {
            return;
        }
        //Reset all attributes on the item.
        item.setAttributes(null);
        World.destroyFloorItem(item);
        player.getInventory().addItem(item).onFailure(it -> World.spawnFloorItem(it, player, 100, 200));
    }

    @Override
    public void handle(final Player player, final FloorItem item, final int optionId, final String option) {
        //Reset all attributes on the item.
        item.setAttributes(null);
        World.takeFloorItem(player, item);
    }

    @Override
    public int[] getItems() {
        final IntOpenHashSet set = new IntOpenHashSet();
        for (final Reanimation reanimation : Reanimation.values()) {
            for (int itemId : reanimation.getItemId())
                set.add(itemId);
        }
        return set.toIntArray();
    }
}
