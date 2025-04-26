package com.zenyte.plugins.flooritem;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 25/04/2019 12:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DarkEssenceFragmentsFloorItemPlugin implements FloorItemPlugin {

    @Override
    public boolean overrideTake() {
        return true;
    }

    @Override
    public boolean canTelegrab(@NotNull final Player player, @NotNull final FloorItem item) {
        if (player.getInventory().containsItem(item)) {
            player.sendMessage("You can't carry that many fragments all at once.");
            return false;
        }
        return true;
    }

    @Override
    public void handle(final Player player, final FloorItem item, final int optionId, final String option) {
        if (option.equals("Take")) {
            if (player.getInventory().containsItem(item)) {
                player.sendMessage("You can't carry that many fragments all at once.");
                return;
            }
            World.takeFloorItem(player, item);
        }
    }

    @Override
    public int[] getItems() {
        return new int[] {
                7938
        };
    }
}
