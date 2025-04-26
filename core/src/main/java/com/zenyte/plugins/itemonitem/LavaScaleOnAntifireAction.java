package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * @author Tommeh | 12/06/2019 | 16:54
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class LavaScaleOnAntifireAction implements ItemOnItemAction {
    public static final Int2IntOpenHashMap POTS = new Int2IntOpenHashMap(new int[] {2452, 2454, 2456, 2458}, new int[] {11951, 11953, 11955, 11957});

    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        if (player.getSkills().getLevel(SkillConstants.HERBLORE) < 84) {
            player.sendMessage("You need a Herblore level of at least 84 to brew extended antifire potions.");
            return;
        }
        final Item potion = from.getId() >= 2452 && from.getId() <= 2458 ? from : to;
        final int slot = player.getInventory().getItem(fromSlot).getId() == potion.getId() ? fromSlot : toSlot;
        final Integer dose = Integer.valueOf(potion.getName().substring(16, 17));
        if (!player.getInventory().containsItem(11994, dose)) {
            player.sendMessage("You don't have enough lava scale shards.");
            return;
        }
        player.getInventory().deleteItem(11994, dose);
        player.getInventory().set(slot, new Item(POTS.get(potion.getId())));
        player.getSkills().addXp(SkillConstants.HERBLORE, 27.5 * dose);
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(2452, 11994), ItemPair.of(2454, 11994), ItemPair.of(2456, 11994), ItemPair.of(2458, 11994)};
    }

    @Override
    public int[] getItems() {
        return null;
    }
}
