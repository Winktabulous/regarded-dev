package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Tommeh | 14/06/2019 | 15:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class UnstrungBallistaCreationAction implements ItemOnItemAction {
    private static final Item UNSTRUNG_LIGHT_BALLISTA = new Item(19604);
    private static final Item UNSTRUNG_HEAVY_BALLISTA = new Item(19607);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (player.getSkills().getLevel(SkillConstants.FLETCHING) < 73) {
            player.sendMessage("You need a Fletching level of at least 73 to do that.");
            return;
        }
        final Item ballista = from.getId() == 19595 || from.getId() == 19598 ? from : to;
        final Item spring = from.getId() == 19601 ? from : to;
        player.getInventory().deleteItemsIfContains(new Item[] {ballista, spring}, () -> {
            if (ballista.getId() == 19595) {
                player.getInventory().addItem(UNSTRUNG_LIGHT_BALLISTA);
            } else {
                player.getInventory().addItem(UNSTRUNG_HEAVY_BALLISTA);
            }
            player.getSkills().addXp(SkillConstants.FLETCHING, 220);
            player.sendMessage("You carefully apply the ballista spring on the " + ballista.getName().toLowerCase() + ".");
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(19595, 19601), ItemPair.of(19598, 19601)};
    }

    @Override
    public int[] getItems() {
        return null;
    }
}
