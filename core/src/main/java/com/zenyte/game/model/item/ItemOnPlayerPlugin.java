package com.zenyte.game.model.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 15/06/2019 10:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface ItemOnPlayerPlugin extends Plugin {

    void handleItemOnPlayerAction(final Player player, final Item item, final int slot, final Player target);

    int[] getItems();

    default void handle(final Player player, final Item item, final int slot, final Player target) {
        player.setRouteEvent(new EntityEvent(player, new EntityStrategy(target), () -> {
            player.stopAll();
            if (item.getId() != ItemId.ROTTEN_POTATO) {
                player.faceEntity(target);
            }
            if (player.getInventory().getItem(slot) != item) {
                return;
            }
            handleItemOnPlayerAction(player, item, slot, target);
        }, true));
    }

    default boolean isUnavailable(@NotNull final Player player) {
        return player.isDead() || player.isLocked() || player.isFinished();
    }

}
