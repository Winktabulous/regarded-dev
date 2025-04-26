package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25. aug 2018 : 19:02:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class TomeOfFire extends ItemPlugin implements PairedItemOnItemPlugin {

    public static final int TOME_OF_FIRE = ItemId.TOME_OF_FIRE;
    public static final int TOME_OF_FIRE_EMPTY = ItemId.TOME_OF_FIRE_EMPTY;
    public static final int BURNT_PAGE = ItemId.BURNT_PAGE;

    public static void handleAction(final Player player, final Item item, final int slotId, final String option, final Container container) {
        if (option.equals("Add pages")) {
            addPages(player, item, slotId, container);
        } else if (option.equalsIgnoreCase("Add or Remove pages")) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options(TITLE, new DialogueOption("Add all pages", () -> addPages(player, item, slotId, container)), new DialogueOption("Remove pages", () -> removePages(player, item, slotId, container)));
                }
            });
        }
    }

    private static void addPages(final Player player, final Item item, final int slotId, final Container container) {
        player.getDialogueManager().finish();
        if (item.getCharges() >= 19981) {
            player.sendMessage("The Tome of Fire can't hold anymore pages.");
            return;
        }
        int amount = player.getInventory().getAmountOf(BURNT_PAGE);
        if (amount == 0) {
            player.sendMessage("You need some burnt pages to fill the Tome of Fire.");
            return;
        }
        if ((amount * 20) + item.getCharges() > 20000) {
            amount = (20000 - item.getCharges()) / 20;
        }
        item.setCharges(item.getCharges() + (amount * 20));
        player.sendMessage("Your tome currently holds " + item.getCharges() + " charges.");
        player.getInventory().deleteItem(BURNT_PAGE, amount);
        if (item.getId() != TOME_OF_FIRE) {
            item.setId(TOME_OF_FIRE);
            container.refresh(slotId);
        }
    }

    private static void removePages(final Player player, final Item item, final int slotId, final Container container) {
        player.getDialogueManager().finish();
        if (item.getCharges() <= 19) {
            player.sendMessage("There are no complete pages remaining in this Tome of Fire.");
            return;
        }
        if (!player.getInventory().hasFreeSlots() && !player.getInventory().containsItem(BURNT_PAGE, 1)) {
            player.sendMessage("You need some more free space to remove the pages from the Tome of Fire.");
            return;
        }
        player.sendInputInt("How many pages would you like to remove?", value -> {
            if (value > (item.getCharges() / 20)) {
                value = (item.getCharges() / 20);
            }
            if (value <= 0) {
                return;
            }
            player.getInventory().addItem(BURNT_PAGE, value);
            item.setCharges(item.getCharges() - (value * 20));
            player.sendMessage("You remove " + value + " page" + (value > 1 ? "s" : "") + " from the Tome of Fire.");
            if (item.getCharges() == 0) {
                item.setId(TOME_OF_FIRE_EMPTY);
                container.refresh(slotId);
            }
        });
    }

    @Override
    public void handle() {
        bind("Pages", (player, item, container, slotId) -> handleAction(player, item, slotId, "Add or Remove pages", container));
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item burntPages = from.getId() == BURNT_PAGE ? from : to;
        final Item book = burntPages == from ? to : from;
        handleAction(player, book, book == from ? fromSlot : toSlot, "Add pages", player.getInventory().getContainer());
    }

    @Override
    public int[] getItems() {
        return new int[] {TOME_OF_FIRE, TOME_OF_FIRE_EMPTY};
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(BURNT_PAGE, TOME_OF_FIRE_EMPTY), ItemPair.of(BURNT_PAGE, TOME_OF_FIRE)};
    }
}
