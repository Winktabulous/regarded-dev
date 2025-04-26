package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Corey
 * @since 17/12/2019
 */
public class CreateGhostCostume implements ItemOnItemAction {
    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final ArrayList<String> itemsRequired = new ArrayList<String>();
        if (!player.getInventory().containsItem(new Item(ChristmasConstants.CHAINS_ID))) {
            itemsRequired.add("something clanky");
        }
        if (!player.getInventory().containsItem(new Item(ChristmasConstants.BEDSHEETS_ID))) {
            itemsRequired.add("some sheets");
        }
        if (!player.getInventory().containsItems(new Item(ItemId.NEEDLE), new Item(ItemId.THREAD))) {
            itemsRequired.add("a needle and thread");
        }
        if (!itemsRequired.isEmpty()) {
            final String requiredString = String.join(", ", itemsRequired);
            player.sendMessage("You can't create the Ghost costume yet, you still need: " + Colour.BLUE.wrap(requiredString) + ".");
            player.sendMessage("Try searching through Scourge's belongings to see what you can find...");
            return;
        }
        player.getInventory().deleteItems(new Item(ItemId.NEEDLE, 1), new Item(ItemId.THREAD, 1), new Item(ChristmasConstants.CHAINS_ID), new Item(ChristmasConstants.BEDSHEETS_ID));
        player.sendMessage("You start cutting up and sewing together the bedsheets.");
        player.lock(7);
        final List<Integer> trackedHolidayItems = player.getTrackedHolidayItems();
        trackedHolidayItems.add(ChristmasConstants.GHOST_HOOD_ID);
        trackedHolidayItems.add(ChristmasConstants.GHOST_TOP_ID);
        trackedHolidayItems.add(ChristmasConstants.GHOST_BOTTOMS_ID);
        WorldTasksManager.schedule(() -> {
            player.sendMessage("You make a ghost hood out of the bedsheets.");
            player.getInventory().addOrDrop(new Item(ChristmasConstants.GHOST_HOOD_ID));
        }, 2);
        WorldTasksManager.schedule(() -> {
            player.sendMessage("You make a ghost top out of the bedsheets and wrap some chains around it.");
            player.getInventory().addOrDrop(new Item(ChristmasConstants.GHOST_TOP_ID));
        }, 4);
        WorldTasksManager.schedule(() -> {
            player.sendMessage("You make some ghost bottoms out of the bedsheets and wrap some chains around them.");
            player.getInventory().addOrDrop(new Item(ChristmasConstants.GHOST_BOTTOMS_ID));
            if (!AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.HAS_GHOST_COSTUME)) {
                player.getDialogueManager().start(new Dialogue(player, ChristmasConstants.PERSONAL_SNOW_IMP) {
                    @Override
                    public void buildDialogue() {
                        final String impName = ChristmasUtils.getImpName(player);
                        npc(impName, "Put it on, put it on!", Expression.HIGH_REV_NORMAL);
                    }
                });
                AChristmasWarble.progress(player, AChristmasWarble.ChristmasWarbleProgress.HAS_GHOST_COSTUME);
            }
        }, 6);
    }

    @Override
    public int[] getItems() {
        return new int[] {ChristmasConstants.BEDSHEETS_ID, ChristmasConstants.CHAINS_ID, ItemId.THREAD, ItemId.NEEDLE};
    }

    @Override
    public boolean allItems() {
        return true;
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {new ItemPair(ChristmasConstants.BEDSHEETS_ID, ItemId.NEEDLE), new ItemPair(ChristmasConstants.BEDSHEETS_ID, ChristmasConstants.CHAINS_ID), new ItemPair(ChristmasConstants.CHAINS_ID, ItemId.NEEDLE)};
    }
}
