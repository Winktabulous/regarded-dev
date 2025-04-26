package com.zenyte.game.content.skills.farming.plugins.supercompost;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Christopher
 * @since 02/24/2020
 */
public class VolcanicAshOnSupercompost implements PairedItemOnItemPlugin {
    private static final Item ultraCompost = new Item(ItemId.ULTRACOMPOST);
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        player.getDialogueManager().start(new UltracompostCreationDialogue(player, ultraCompost));
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {
                ItemPair.of(ItemId.SUPERCOMPOST, ItemId.VOLCANIC_ASH)
        };
    }
}
