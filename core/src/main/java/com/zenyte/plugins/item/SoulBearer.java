package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.magic.spells.arceuus.Reanimation;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.plugins.dialogue.ItemChat;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * @author Kris | 12/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SoulBearer extends ItemPlugin implements PairedItemOnItemPlugin {
    @Override
    public void handle() {
        bind("Fill", (player, item, container, slotId) -> {
            if (item.getCharges() >= 1000) {
                player.sendMessage("Your soul bearer is full.");
                return;
            }
            final Inventory inventory = player.getInventory();
            if (!inventory.containsItem(ItemId.SOUL_RUNE, 1) || !inventory.containsItem(ItemId.BLOOD_RUNE, 1)) {
                player.sendMessage("You need at least one soul and one blood rune to fill the soul bearer.");
                return;
            }
            final int minimum = Math.min(1000 - item.getCharges(), Math.min(inventory.getAmountOf(ItemId.SOUL_RUNE), inventory.getAmountOf(ItemId.BLOOD_RUNE)));
            if (minimum <= 0) {
                return;
            }
            inventory.deleteItem(ItemId.SOUL_RUNE, minimum);
            inventory.deleteItem(ItemId.BLOOD_RUNE, minimum);
            item.setCharges(item.getCharges() + minimum);
            player.getDialogueManager().start(new ItemChat(player, item, "You add " + minimum + " charge" + (minimum == 1 ? "" : "s") + " to your soul bearer.<br>It now has " + item.getCharges() + " charge" + (item.getCharges() == 1 ? "" : "s") + "."));
        });
        bind("Check", (player, item, container, slotId) -> player.sendMessage(item.getCharges() <= 0 ? "Your soul bearer has no charges. It feels on soul runes and blood runes." : ("Your soul bearer has " + item.getCharges() + " charge" + (item.getCharges() == 1 ? "" : "s") + ".")));
        bind("Uncharge", (player, item, container, slotId) -> {
            if (player.getDuel() != null) {
                player.sendMessage("You cannot uncharge the soul bearer within duels.");
                return;
            }
            if (item.getCharges() <= 0) {
                player.sendMessage("Your soul bearer is empty.");
                return;
            }
            final Inventory inventory = player.getInventory();
            if (inventory.getFreeSlots() < 2) {
                player.sendMessage("You need some more free inventory space to do this.");
                return;
            }
            inventory.addOrDrop(new Item(ItemId.SOUL_RUNE, item.getCharges()));
            inventory.addOrDrop(new Item(ItemId.BLOOD_RUNE, item.getCharges()));
            item.setCharges(0);
            player.getDialogueManager().start(new ItemChat(player, item, "You remove all the charges from your soul bearer. It is now empty."));
        });
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Inventory inventory = player.getInventory();
        final Item bearer = from.getId() == ItemId.SOUL_BEARER ? from : to;
        final Item ensouledHead = bearer == from ? to : from;
        final int amount = Math.min(inventory.getAmountOf(ensouledHead.getId()), bearer.getCharges());
        if (amount <= 0) {
            player.sendMessage("Your soul bearer is empty.");
            return;
        }
        final int succeededAmount = player.getBank().add(new Item(ensouledHead.getId(), amount)).getSucceededAmount();
        if (succeededAmount <= 0) {
            player.sendMessage("Your bank is full.");
            return;
        }
        inventory.deleteItem(new Item(ensouledHead.getId(), succeededAmount));
        bearer.setCharges(bearer.getCharges() - succeededAmount);
        player.getDialogueManager().start(new ItemChat(player, bearer, "Your soul bearer carries the ensouled heads to your bank. It has " + bearer.getCharges() + " charge" + (bearer.getCharges() == 1 ? "" : "s") + " left."));
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.SOUL_BEARER};
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        final ObjectArrayList<ItemOnItemAction.ItemPair> list = new ObjectArrayList<ItemPair>();
        for (final Reanimation reanimation : Reanimation.values()) {
            for (int i : reanimation.getItemId()) {
                list.add(ItemPair.of(ItemId.SOUL_BEARER, i));

            }
        }
        return list.toArray(new ItemPair[0]);
    }
}
