package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

public class CutBarbarianFishAction implements ItemOnItemAction {

    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {

        if(from.getId() != 946 && to.getId() != 946 || from.getId() == 946 && to.getId() == 946) {
            player.sendMessage("Nothing interesting happens.");
            return;
        }

        if(from.getId() == 11328 || to.getId() == 11328) {
            player.getInventory().deleteItem(11328, 1);
            player.getInventory().addItem(11324, 1);
            player.sendFilteredMessage("You cut open the fish and extract some roe, but the rest of the fish is reduced to useless fragments, which you discard.");
            player.getSkills().addXp(SkillConstants.COOKING, 10);
        }

        if(from.getId() == 11330 || to.getId() == 11330) {
            player.getInventory().deleteItem(11330, 1);
            player.getInventory().addItem(11324, 1);
            player.sendFilteredMessage("You cut open the fish and extract some roe, but the rest of the fish is reduced to useless fragments, which you discard.");
            player.getSkills().addXp(SkillConstants.COOKING, 10);
        }

        if(from.getId() == 11332 || to.getId() == 11332) {
            player.getInventory().deleteItem(11332, 1);
            player.getInventory().addItem(11326, 1);
            player.sendFilteredMessage("You cut open the fish and extract some caviar, but the rest of the fish is reduced to useless fragments, which you discard.");
            player.getSkills().addXp(SkillConstants.COOKING, 15);
        }


    }

    @Override
    public int[] getItems() {
        return new int[] { 946, 11328, 11330, 11332 };
    }
}
