package com.zenyte.plugins.item;

import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;
import java.util.Optional;

/**
 * @author Kris | 25/08/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SherlocksNotes extends ItemPlugin implements PairedItemOnItemPlugin {
    private static final int SHERLOCKS_NOTES = 30210;

    @Override
    public void handle() {
        bind("Info", (player, item, container, slotId) -> player.getDialogueManager().start(new ItemChat(player, item, "Sherlock's notes can be used on any clue scroll to advance one stage further to completing it.<br>Just use the notes on a clue scroll in your inventory to activate it.")));
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item paper = from.getId() == SHERLOCKS_NOTES ? from : to;
        final Item clue = paper == from ? to : from;
        final List<String> list = TreasureTrail.getCluesList(clue);
        if (list == null) {
            TreasureTrail.setRandomClue(clue);
        }
        player.getInventory().deleteItem(new Item(paper.getId(), 1));
        TreasureTrail.progress(player, clue, clue == from ? fromSlot : toSlot, Optional.of(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                item(paper, "You use Sherlock's notes on the clue scroll and solve the riddle.");
            }
        }));
    }

    @Override
    public int[] getItems() {
        return new int[] {SHERLOCKS_NOTES};
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        final ObjectArrayList<ItemOnItemAction.ItemPair> list = new ObjectArrayList<ItemPair>();
        for (final int clue : ClueItem.getCluesArray()) {
            list.add(ItemPair.of(SHERLOCKS_NOTES, clue));
        }
        return list.toArray(new ItemPair[0]);
    }
}
