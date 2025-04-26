package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.plugins.dialogue.TzHaarMejJalD;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class FirecapeOnTzHaarMejJal implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage(Colour.RS_RED.wrap("You need to make space in your inventory!"));
            return;
        }
        player.getDialogueManager().start(new TzHaarMejJalD(player, npc, true));
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 6570 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CATAPULT_2180 };
    }
}
