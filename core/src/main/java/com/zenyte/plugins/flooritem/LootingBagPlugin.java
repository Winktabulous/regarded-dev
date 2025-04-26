package com.zenyte.plugins.flooritem;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.LootingBag;
import com.zenyte.game.world.flooritem.FloorItem;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 22/04/2019 15:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LootingBagPlugin implements FloorItemPlugin {

    @Override
    public void handle(final Player player, final FloorItem item, final int optionId, final String option) {
        if (option.equalsIgnoreCase("take")) {
            if (LootingBag.hasBag(player)) {
                player.sendMessage("You can only own one looting bag at a time.");
                return;
            }
            World.takeFloorItem(player, item);
        }
    }

    @Override
    public boolean canTelegrab(@NotNull final Player player, @NotNull final FloorItem item) {
        if (LootingBag.hasBag(player)) {
            player.sendMessage("You can only own one looting bag at a time.");
            return false;
        }
        return true;
    }

    @Override
    public boolean overrideTake() {
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[] { 11941, 22586 };
    }

}
