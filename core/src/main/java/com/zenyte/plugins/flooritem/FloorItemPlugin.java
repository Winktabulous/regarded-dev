package com.zenyte.plugins.flooritem;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.plugins.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 27. march 2018 : 21:57.56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface FloorItemPlugin extends Plugin {

	void handle(final Player player, final FloorItem item, final int optionId, final String option);
	
	int[] getItems();
	
	default boolean overrideTake() {
		return false;
	}

	default boolean canTelegrab(@NotNull final Player player, @NotNull final FloorItem item) {
	    return true;
    }

    default void telegrab(@NotNull final Player player, @NotNull final FloorItem item) {
        if (!canTelegrab(player, item)) {
            return;
        }
        player.log(LogLevel.INFO, "Telegrabbed item '" + item + "'.");
        World.destroyFloorItem(item);
        player.getInventory().addItem(item).onFailure(it -> World.spawnFloorItem(it, player, 100, 200));
    }

}
