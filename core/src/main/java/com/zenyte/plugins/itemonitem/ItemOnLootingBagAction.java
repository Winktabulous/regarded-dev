package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Tommeh | 27-2-2019 | 18:12
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ItemOnLootingBagAction implements ItemOnItemAction {
    private static String[] getOptions(final Item item) {
        if (item.getAmount() >= 2 && item.getAmount() <= 5) {
            return new String[] {"One", "All", "X"};
        } else {
            return new String[] {"One", "Five", "All", "X"};
        }
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item bag = from.getId() == 11941 ? from : to;
        final Item item = from.getId() != 11941 ? from : to;
        final int itemSlot = from.getId() != 11941 ? fromSlot : toSlot;
        if (item.getId() == bag.getId()) {
            player.sendMessage("Nothing interesting happens.");
            return;
        }
        if (!item.isTradable()) {
            player.sendMessage("Only tradeable items can be put in the bag.");
            return;
        }
        if (!WildernessArea.isWithinWilderness(player)) {
            player.sendMessage("You can't put items in the looting bag unless you're in the Wilderness.");
            return;
        }
        final ItemDefinitions def = item.getDefinitions();
        final int amount = player.getInventory().getAmountOf(item.getId());
        if (amount > 1 && player.getBooleanAttribute("looting_bag_amount_prompt")) {
            final String[] options = getOptions(item);
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("How many do you want to deposit?", options).onOptionOne(() -> player.getLootingBag().deposit(itemSlot, 1)).onOptionTwo(() -> player.getLootingBag().deposit(itemSlot, options[1].equals("All") ? item.getAmount() : 5)).onOptionThree(() -> {
                        if (options[2].equals("All")) {
                            player.getLootingBag().deposit(itemSlot, amount);
                        } else {
                            finish();
                            player.sendInputInt("Enter amount to store:", amount -> player.getLootingBag().deposit(itemSlot, amount));
                        }
                    }).onOptionFour(() -> {
                        finish();
                        player.sendInputInt("Enter amount to store:", amount -> player.getLootingBag().deposit(itemSlot, amount));
                    });
                }
            });
        } else {
            for (final int slot : player.getInventory().getContainer().getSlotsOf(item.getId())) {
                player.getLootingBag().deposit(slot, item.getAmount());
            }
        }
    }

    @Override
    public boolean allItems() {
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[] {11941, 22586};
    }
}
