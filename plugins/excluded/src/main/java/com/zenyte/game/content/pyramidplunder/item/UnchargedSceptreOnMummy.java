package com.zenyte.game.content.pyramidplunder.item;

import com.zenyte.game.content.pyramidplunder.npc.GuardianMummyDialogue;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemChain;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Chris
 * @since May 20 2020
 */
public class UnchargedSceptreOnMummy implements ItemOnNPCAction {
    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        player.getDialogueManager().start(new GuardianMummyDialogue(player, npc, true));
    }

    @Override
    public Object[] getItems() {
        return new Object[] {ItemChain.PHARAOH_SCEPTRE.first()};
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {NpcId.GUARDIAN_MUMMY};
    }
}
