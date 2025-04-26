package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.AlternateMaxCape;
import com.zenyte.game.content.MaxCape;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.ints.IntArrayList;

@SuppressWarnings("unused")
public class ItemOnAlternateMaxCapeAction implements ItemOnItemAction {

    IntArrayList capes = new IntArrayList();

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item cape = capes.contains(from.getId()) ? from : to;
        final AlternateMaxCape upgraded = AlternateMaxCape.get(capes.contains(from.getId()) ? from.getId() : to.getId());
        if (upgraded == null) {
            return;
        }
        final Item upgrade = new Item(upgraded.getUpgrade());
        final Item upgradedCape = new Item(upgraded.getCape());
        final Item upgradedHood = new Item(upgraded.getHood());
        if (!player.getInventory().containsItem(upgraded.getAlternateHood())) {
            player.sendMessage("You need your max cape hood to be able to combine those.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("You will lose your " + upgrade.getName() + " in the process.", "Yes - combine this item with your max cape and lose it.", "No! I don't want to lose my item.").onOptionOne(() -> {
                    setKey(5);
                    player.getInventory().deleteItem(cape);
                    player.getInventory().deleteItem(upgrade);
                    player.getInventory().deleteItem(new Item(upgraded.getAlternateHood()));
                    player.getInventory().addItem(upgradedCape);
                    player.getInventory().addItem(upgradedHood);
                });
                item(5, upgradedCape, "You combine your " + upgrade.getName() + " and max cape as one.");
            }
        });
    }

    @Override
    public ItemOnItemAction.ItemPair[] getMatchingPairs() {
        final ItemOnItemAction.ItemPair[] pairs = new ItemOnItemAction.ItemPair[AlternateMaxCape.values.length];
        for (int i = 0; i < pairs.length; i++) {
            final AlternateMaxCape cape = AlternateMaxCape.values[i];
            capes.add(cape.getAlternateCape());
            pairs[i] = new ItemOnItemAction.ItemPair(cape.getAlternateCape(), cape.getUpgrade());
        }
        return pairs;
    }

    @Override
    public int[] getItems() {
        return null;
    }
}
