package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.model.item.enums.FungicideSpray;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;

import java.util.ArrayList;

/**
 * @author Tommeh | 23-3-2019 | 19:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class FungicideOnZygomiteNPCAction implements ItemOnNPCAction {
    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        final FungicideSpray spray = FungicideSpray.get(item.getId());
        if (spray.equals(FungicideSpray.EMPTY)) {
            player.sendMessage("Your fungicide spray is currently empty.");
            return;
        }
        if (npc.getHitpoints() <= 7) {
            npc.getTemporaryAttributes().put("used_fungicide_spray", new Object[] {spray, slot});
            npc.sendDeath();
        } else {
            player.sendMessage("The zygomite isn't weak enough to be affected by the fungicide.");
        }
    }

    @Override
    public Object[] getItems() {
        final ArrayList<Object> list = new ArrayList<Object>();
        FungicideSpray.ALL.forEach(spray -> list.add(spray.getId()));
        return list.toArray(new Object[list.size()]);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {"Zygomite", "Ancient Zygomite"};
    }
}
