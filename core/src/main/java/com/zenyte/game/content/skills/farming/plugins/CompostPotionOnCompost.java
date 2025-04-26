package com.zenyte.game.content.skills.farming.plugins;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Kris | 22/05/2019 14:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CompostPotionOnCompost implements PairedItemOnItemPlugin {
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Item compost = from.getId() == 6032 ? from : to;
        final Item potion = compost == from ? to : from;
        final int compostSlot = compost == from ? fromSlot : toSlot;
        final int potionSlot = potion == from ? fromSlot : toSlot;
        final Inventory inventory = player.getInventory();
        inventory.set(potionSlot, potion.getId() == 6476 ? new Item(229) : (new Item(potion.getId() + 2)));
        inventory.set(compostSlot, new Item(6034));
        player.sendMessage("You pour the compost potion on your bucket of compost.");
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(6470, 6032), ItemPair.of(6472, 6032), ItemPair.of(6474, 6032), ItemPair.of(6476, 6032)};
    }
}
