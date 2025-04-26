package com.zenyte.game.content.itemtransportation.masterscrolls;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.items.ItemDefinitions;

import java.util.Optional;

/**
 * @author Kris | 18/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MasterScrollBook extends ItemPlugin implements PairedItemOnItemPlugin {
    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> {
            player.getTemporaryAttributes().put("Master scroll book item attr", item);
            GameInterface.MASTER_SCROLL_BOOK.open(player);
        });
        bind("Remove default", (player, item, slotId) -> MasterScrollBookInterface.removeFavourite(player));
        bind("Teleport", (player, item, slotId) -> {
            final Optional<ScrollBookTeleport> optionalFavourite = MasterScrollBookInterface.getFavourite(player);
            if (!optionalFavourite.isPresent()) {
                player.sendMessage("You haven\'t selected which teleport scroll you want to use as your default teleport.");
                return;
            }
            MasterScrollBookInterface.activate(player, item, optionalFavourite.get());
        });
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item book = from.getId() == ItemId.MASTER_SCROLL_BOOK_EMPTY || from.getId() == ItemId.MASTER_SCROLL_BOOK ? from : to;
        final Item scrolls = from == book ? to : from;
        final int count = MasterScrollBookInterface.getCount(book, scrolls.getId());
        final int addableCount = Math.min(MasterScrollBookInterface.MAXIMUM_TELEPORTS_COUNT - count, scrolls.getAmount());
        if (addableCount <= 0) {
            player.sendMessage("Your Master Scroll Book cannot hold any more of these scrolls.");
            return;
        }
        MasterScrollBookInterface.setCount(book, scrolls.getId(), count + addableCount);
        player.getInventory().deleteItem(new Item(scrolls.getId(), addableCount));
        player.sendMessage("Your Master Scroll Book is now storing " + (count + addableCount) + " " + ItemDefinitions.getOrThrow(scrolls.getId()).getName() + ((count + addableCount) == 1 ? "" : "s") + ".");
        book.setId(ItemId.MASTER_SCROLL_BOOK);
        player.getInventory().refreshAll();
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.MASTER_SCROLL_BOOK, ItemId.MASTER_SCROLL_BOOK_EMPTY};
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        final ObjectArrayList<ItemOnItemAction.ItemPair> pairs = new ObjectArrayList<ItemPair>();
        for (final ScrollBookTeleport teleport : ScrollBookTeleport.values) {
            pairs.add(ItemPair.of(ItemId.MASTER_SCROLL_BOOK, teleport.getScrollId()));
            pairs.add(ItemPair.of(ItemId.MASTER_SCROLL_BOOK_EMPTY, teleport.getScrollId()));
        }
        return pairs.toArray(new ItemPair[0]);
    }
}
