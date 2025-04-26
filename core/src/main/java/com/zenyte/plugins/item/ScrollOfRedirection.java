package com.zenyte.plugins.item;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Kris | 09/09/2019 19:12
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ScrollOfRedirection extends ItemPlugin {
    @Override
    public void handle() {
        bind("Refund", (player, item, container, slotId) -> player.sendInputInt("How many scrolls would you like to exchange back to loyalty points?", value -> {
            final Inventory inventory = player.getInventory();
            if (inventory.getItem(slotId) != item) {
                return;
            }
            final int amount = Math.min(value, item.getAmount());
            if (amount < 1) {
                return;
            }
            inventory.deleteItem(new Item(item.getId(), amount));
            player.getLoyaltyManager().setLoyaltyPoints(player.getLoyaltyManager().getLoyaltyPoints() + amount);
            GameInterface.GAME_NOTICEBOARD.getPlugin().ifPresent(plugin -> player.getPacketDispatcher().sendComponentText(plugin.getInterface(), plugin.getComponent("Loyalty points"), "Loyalty points: <col=ffffff>" + player.getLoyaltyManager().getLoyaltyPoints() + "</col>"));
        }));
    }

    @Override
    public int[] getItems() {
        return new int[] {11740};
    }
}
