package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 09/09/2019 18:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TabletRedirecting implements PairedItemOnItemPlugin {

    private enum RedirectionDestination {
        RIMMINGTON(11741, 1), TAVERLEY(11742, 10), POLLNIVNEACH(11743, 20), HOSIDIUS(19651, 25), RELLEKKA(11744, 30), BRIMHAVEN(11745, 40), YANILLE(11746, 50), TROLLHEIM(11747, 1);
        private final int id;
        private final int level;

        RedirectionDestination(int id, int level) {
            this.id = id;
            this.level = level;
        }
    }

    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Redirect where?", new DialogueOption("Rimmington", () -> redirect(player, from, to, fromSlot, toSlot, RedirectionDestination.RIMMINGTON)), new DialogueOption("Taverley", () -> redirect(player, from, to, fromSlot, toSlot, RedirectionDestination.TAVERLEY)), new DialogueOption("Pollnivneach", () -> redirect(player, from, to, fromSlot, toSlot, RedirectionDestination.POLLNIVNEACH)), new DialogueOption("Hosidius", () -> redirect(player, from, to, fromSlot, toSlot, RedirectionDestination.HOSIDIUS)), new DialogueOption("More...", key(100)));
                options(100, "Redirect where?", new DialogueOption("Rellekka", () -> redirect(player, from, to, fromSlot, toSlot, RedirectionDestination.RELLEKKA)), new DialogueOption("Brimhaven", () -> redirect(player, from, to, fromSlot, toSlot, RedirectionDestination.BRIMHAVEN)), new DialogueOption("Yanille", () -> redirect(player, from, to, fromSlot, toSlot, RedirectionDestination.YANILLE)), new DialogueOption("Trollheim", () -> redirect(player, from, to, fromSlot, toSlot, RedirectionDestination.TROLLHEIM)), new DialogueOption("Previous...", key(1)));
            }
        });
    }

    private final void redirect(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot, final RedirectionDestination destination) {
        player.getDialogueManager().finish();
        final Item houseTab = from.getId() == 8013 ? from : to;
        final Item scrolls = from == houseTab ? to : from;
        player.sendInputInt("How many would you like to redirect to " + destination.toString().toLowerCase() + "? (1-" + Math.min(houseTab.getAmount(), scrolls.getAmount()) + ")", value -> {
            if (player.getSkills().getLevelForXp(SkillConstants.CONSTRUCTION) < destination.level) {
                player.sendMessage("You need a Construction level of at least " + destination.level + " to redirect the scrolls to " + destination.toString().toLowerCase() + ".");
                return;
            }
            final Inventory inventory = player.getInventory();
            if (inventory.getItem(fromSlot) != from || inventory.getItem(toSlot) != to) {
                return;
            }
            if (!inventory.hasFreeSlots() && !inventory.containsItem(destination.id, 1)) {
                player.sendMessage("You need some free inventory space to redirect the scrolls.");
                return;
            }
            final int amount = Math.min(value, Math.min(houseTab.getAmount(), scrolls.getAmount()));
            if (amount < 1) {
                return;
            }
            inventory.deleteItem(houseTab.getId(), amount);
            inventory.deleteItem(scrolls.getId(), amount);
            inventory.addOrDrop(new Item(destination.id, amount));
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(8013, 11740)};
    }
}
