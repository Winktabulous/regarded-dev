package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 31-1-2019 | 18:31
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class AncientShardOnArclightAction implements ItemOnItemAction {
    private static final int MAX_CHARGES = 10000;

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item arclight = from.getId() == 19675 ? from : to;
        final Item shards = from.getId() == 19677 ? from : to;
        final int currentCharges = arclight.getCharges();
        final int shardsToUse = Math.min((MAX_CHARGES - arclight.getCharges()) / 1000 * 3, shards.getAmount());
        final int extraCharges = (shardsToUse / 3) * 1000;
        final int result = Math.min(MAX_CHARGES, currentCharges + extraCharges);
        if (!player.getInventory().containsItem(19677, 3)) {
            player.sendMessage("You need a minimum of three ancient shards to charge the arclight.");
            return;
        }
        if (currentCharges >= MAX_CHARGES) {
            //might want to remove, on osrs it doesn't check if it's fully charged so u lose charges
            player.sendMessage("Your arclight is already fully charged!");
            return;
        }
        arclight.setCharges(result);
        player.getInventory().deleteItem(shards.getId(), (shardsToUse / 3) * 3);
    }

    @Override
    public int[] getItems() {
        return new int[] {19675, 19677};
    }
}
