package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Tommeh | 14/06/2019 | 15:31
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class BallistaCreationAction implements ItemOnItemAction {
    private static final Item LIGHT_BALLISTA = new Item(19478);
    private static final Item HEAVY_BALLISTA = new Item(19481);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (player.getSkills().getLevel(SkillConstants.FLETCHING) < 73) {
            player.sendMessage("You need a Fletching level of at least 73 to do that.");
            return;
        }
        final Item ballista = from.getId() == 19604 || from.getId() == 19607 ? from : to;
        final Item tail = from.getId() == 19610 ? from : to;
        player.getInventory().deleteItemsIfContains(new Item[] {ballista, tail}, () -> {
            if (ballista.getId() == 19604) {
                player.getInventory().addItem(LIGHT_BALLISTA);
            } else {
                player.getInventory().addItem(HEAVY_BALLISTA);
            }
            player.getSkills().addXp(SkillConstants.FLETCHING, 220);
            player.sendMessage("You carefully apply the monkey tail on the " + ballista.getName().toLowerCase() + ".");
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(19604, 19610), ItemPair.of(19607, 19610)};
    }

    @Override
    public int[] getItems() {
        return null;
    }
}
