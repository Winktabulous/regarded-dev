package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

/**
 * @author Kris | 19/04/2019 22:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ClueScrollProgresserItem implements PairedItemOnItemPlugin {
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Item orb = from.getId() == 6951 ? from : to;
        final Item clue = from == orb ? to : from;
        TreasureTrail.setRandomClue(clue).view(player, clue);
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        final ObjectOpenHashSet<ItemOnItemAction.ItemPair> pairs = new ObjectOpenHashSet<ItemPair>();
        for (final int clue : ClueItem.getCluesArray()) {
            pairs.add(ItemPair.of(clue, 6951));
        }
        return pairs.toArray(new ItemPair[0]);
    }
}
