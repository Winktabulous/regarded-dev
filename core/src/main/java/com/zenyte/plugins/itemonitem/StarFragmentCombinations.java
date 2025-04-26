package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Andys1814.
 */
public final class StarFragmentCombinations implements PairedItemOnItemPlugin {

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        Item equipment = from.getId() != ItemId.STAR_FRAGMENT ? from : to;
        Item fragment = from.getId() == ItemId.STAR_FRAGMENT ? from : to;

        Item gold;
        if (equipment.getId() == ItemId.PROSPECTOR_HELMET) {
            gold = new Item(ItemId.GOLDEN_PROSPECTOR_HELMET);
        } else if (equipment.getId() == ItemId.PROSPECTOR_JACKET) {
            gold = new Item(ItemId.GOLDEN_PROSPECTOR_JACKET);
        } else if (equipment.getId() == ItemId.PROSPECTOR_LEGS) {
            gold = new Item(ItemId.GOLDEN_PROSPECTOR_LEGS);
        } else if (equipment.getId() == ItemId.PROSPECTOR_BOOTS) {
            gold = new Item(ItemId.GOLDEN_PROSPECTOR_BOOTS);
        } else {
            throw new IllegalArgumentException("Unexpected Star fragment / prospector combination: " + equipment + ", " + fragment + ".");
        }

        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                doubleItem(equipment, fragment, "Combining a Star fragment with a piece of Prospector equipment will recolour that piece gold. The Star fragment will be destroyed, and the process will be irreversible..");
                options("Proceed with the recolour?", new DialogueOption("Yes.", () -> {
                    player.getInventory().deleteItem(equipment.getId(), 1);
                    player.getInventory().addItem(gold.getId(), 1);
                    player.getInventory().deleteItem(ItemId.STAR_FRAGMENT, 1);
                    player.getDialogueManager().start(new Dialogue(player) {
                        @Override
                        public void buildDialogue() {
                            item(gold, "You use a Star fragment to recolour a piece of your Prospector equipment gold.");
                        }
                    });
                    key(5);
                }), new DialogueOption("No."));

            }
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {
            ItemPair.of(ItemId.STAR_FRAGMENT, ItemId.PROSPECTOR_HELMET),
            ItemPair.of(ItemId.STAR_FRAGMENT, ItemId.PROSPECTOR_JACKET),
            ItemPair.of(ItemId.STAR_FRAGMENT, ItemId.PROSPECTOR_LEGS),
            ItemPair.of(ItemId.STAR_FRAGMENT, ItemId.PROSPECTOR_BOOTS),
        };
    }

}
