package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.model.item.degradableitems.RepairableItem;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.RepairItemD;

import java.util.ArrayList;

/**
 * @author Tommeh | 8 sep. 2018 | 22:55:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ItemOnBobNPCAction implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        player.getDialogueManager().start(new RepairItemD(player, item, false));
    }

    @Override
    public Object[] getItems() {
        final ArrayList<Object> list = new ArrayList<Object>();
        for (final RepairableItem repairable : RepairableItem.VALUES) {
            for (final int id : repairable.getIds()) {
                list.add(id);
            }
        }
        return list.toArray(new Object[list.size()]);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 505 };
    }
}
