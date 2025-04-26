package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Tommeh | 14/06/2019 | 15:17
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class IncompleteBallistaCreationAction implements ItemOnItemAction {
    private static final Item INCOMPLETE_LIGHT_BALLISTA = new Item(19595);
    private static final Item INCOMPLETE_HEAVY_BALLISTA = new Item(19598);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (player.getSkills().getLevel(SkillConstants.FLETCHING) < 73) {
            player.sendMessage("You need a Fletching level of at least 73 to do that.");
            return;
        }
        final Item frame = from.getId() == 19586 || from.getId() == 19589 ? from : to;
        final Item limbs = from.getId() == 19592 ? from : to;
        player.getInventory().deleteItemsIfContains(new Item[] {frame, limbs}, () -> {
            if (frame.getId() == 19586) {
                player.getInventory().addItem(INCOMPLETE_LIGHT_BALLISTA);
            } else {
                player.getInventory().addItem(INCOMPLETE_HEAVY_BALLISTA);
            }
            player.getSkills().addXp(SkillConstants.FLETCHING, 220);
            player.sendMessage("You carefully apply the ballista limbs on the " + frame.getName().toLowerCase() + ".");
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(19586, 19592), ItemPair.of(19589, 19592)};
    }

    @Override
    public int[] getItems() {
        return null;
    }
}
