package com.zenyte.game.content.sandstorm;

import com.zenyte.game.content.sandstorm.dialogue.DrewDepositDialogue;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Chris
 * @since August 26 2020
 */
public class BucketOnDrew implements ItemOnNPCAction {
    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        player.getDialogueManager().start(new DrewDepositDialogue(player, npc));
    }

    @Override
    public Object[] getItems() {
        return new Object[]{ItemId.BUCKET, ItemDefinitions.getOrThrow(ItemId.BUCKET).getNotedOrDefault()};
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{NpcId.DREW};
    }
}
