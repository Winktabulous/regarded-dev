package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.content.minigame.pestcontrol.npc.misc.EliteVoidKnight;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 22-4-2019 | 19:17
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class VoidPieceOnEliteVoidKnightNPCAction implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        EliteVoidKnight.upgrade(player, npc, item);
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 8839, 8840 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 5513 };
    }
}
