package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.item.sceptre.AncientSceptre;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-08-29
 */
public class AncientSceptreShadowItemCreation extends AncientSceptre implements ItemOnItemAction {
    @Override
    public int[] getItems() {
        return new int[] { ItemId.SHADOW_QUARTZ, ItemId.ANCIENT_SCEPTRE };
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        var gem = from.getId() == ItemId.SHADOW_QUARTZ ? from : to;
        var sceptre = to.getId() == ItemId.ANCIENT_SCEPTRE ? to : from;

        player.getDialogueManager().start(getScepterCombiningDialogue(player, gem, sceptre, ItemId.SHADOW_ANCIENT_SCEPTRE_28266));
    }
}
